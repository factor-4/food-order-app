package com.example.FoodOrderApp.menu.services;

import com.example.FoodOrderApp.tempservice.CloudinaryService;
import com.example.FoodOrderApp.category.entity.Category;
import com.example.FoodOrderApp.category.repository.CategoryRepository;
import com.example.FoodOrderApp.exceptions.BadRequestException;
import com.example.FoodOrderApp.exceptions.NotFoundException;
import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.example.FoodOrderApp.menu.entity.Menu;
import com.example.FoodOrderApp.menu.repository.MenuRepository;
import com.example.FoodOrderApp.response.Response;
import com.example.FoodOrderApp.review.dtos.ReviewDTO;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    // Helper to extract public ID from a Cloudinary URL
    private String extractPublicIdFromUrl(String url) {
        // Expected format: .../upload/v1234/menus/filename.jpg
        String[] parts = url.split("/");
        // Public ID is "menus/filename" (without extension)
        if (parts.length >= 2) {
            String folder = parts[parts.length - 2];
            String file = parts[parts.length - 1].split("\\.")[0];
            return folder + "/" + file;
        }
        // Fallback
        return parts[parts.length - 1].split("\\.")[0];
    }

    @Override
    public Response<MenuDTO> createMenu(MenuDTO menuDTO) {
        log.info("Inside createMenu()");
        Category category = categoryRepository.findById(menuDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));
        String imageUrl = null;

        MultipartFile imageFile = menuDTO.getImageFile();

        if (imageFile == null || imageFile.isEmpty()) {
            throw new BadRequestException("Menu image is required");
        }

        try {
            imageUrl = cloudinaryService.uploadFile(imageFile, "menus");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        Menu menu = Menu.builder()
                .name(menuDTO.getName())
                .description(menuDTO.getDescription())
                .price(menuDTO.getPrice())
                .imageUrl(imageUrl)
                .category(category)
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return Response.<MenuDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu created successfully")
                .data(modelMapper.map(savedMenu, MenuDTO.class))
                .build();
    }

    @Override
    public Response<MenuDTO> updateMenu(MenuDTO menuDTO) {
        log.info("Inside updateMenu()");

        Menu existingMenu = menuRepository.findById(menuDTO.getId())
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        Category category = categoryRepository.findById(menuDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        String imageUrl = existingMenu.getImageUrl();
        MultipartFile imageFile = menuDTO.getImageFile();

        // Check if new image was provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if it exists
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String publicId = extractPublicIdFromUrl(imageUrl);
                try {
                    cloudinaryService.deleteFile(publicId);
                    log.info("Deleted old menu image: {}", publicId);
                } catch (IOException e) {
                    log.error("Failed to delete old image: {}", e.getMessage());
                }
            }

            // Upload new image
            try {
                imageUrl = cloudinaryService.uploadFile(imageFile, "menus");
                log.info("New image uploaded: {}", imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload new image", e);
            }
        }

        if (menuDTO.getName() != null && !menuDTO.getName().isBlank())
            existingMenu.setName(menuDTO.getName());

        if (menuDTO.getDescription() != null && !menuDTO.getDescription().isBlank())
            existingMenu.setDescription(menuDTO.getDescription());

        if (menuDTO.getPrice() != null)
            existingMenu.setPrice(menuDTO.getPrice());

        existingMenu.setImageUrl(imageUrl);
        existingMenu.setCategory(category);

        Menu updatedMenu = menuRepository.save(existingMenu);

        return Response.<MenuDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu updated successfully")
                .data(modelMapper.map(updatedMenu, MenuDTO.class))
                .build();
    }

    @Override
    public Response<MenuDTO> getMenuById(Long id) {
        log.info("Inside getMenuById()");
        Menu existingMenu = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        MenuDTO menuDTO = modelMapper.map(existingMenu, MenuDTO.class);

        if (menuDTO.getReviews() != null) {
            menuDTO.getReviews().sort(Comparator.comparing(ReviewDTO::getId).reversed());
        }

        return Response.<MenuDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu retrieved successfully")
                .data(menuDTO)
                .build();
    }

    @Override
    public Response<?> deleteMenu(Long id) {
        log.info("Inside deleteMenu()");

        Menu menuToDelete = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        // Delete image from Cloudinary if it exists
        String imageUrl = menuToDelete.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String publicId = extractPublicIdFromUrl(imageUrl);
            try {
                cloudinaryService.deleteFile(publicId);
                log.info("Deleted image: {}", publicId);
            } catch (IOException e) {
                log.error("Failed to delete image: {}", e.getMessage());
            }
        }

        menuRepository.deleteById(id);

        return Response.<MenuDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu deleted successfully")
                .build();
    }

    @Override
    public Response<List<MenuDTO>> getMenus(Long categoryId, String search) {
        log.info("Inside getMenus()");

        Specification<Menu> spec = buildSpecification(categoryId, search);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Menu> menuList = menuRepository.findAll(spec, sort);

        List<MenuDTO> menuDTOS = menuList.stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .toList();

        return Response.<List<MenuDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Menu retrieved")
                .data(menuDTOS)
                .build();
    }

    private Specification<Menu> buildSpecification(Long categoryId, String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (search != null && !search.isBlank()) {
                String searchTerm = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), searchTerm),
                        cb.like(cb.lower(root.get("description")), searchTerm)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}