package com.example.FoodOrderApp.menu.services;

import com.example.FoodOrderApp.aws.AWSS3Service;
import com.example.FoodOrderApp.category.entity.Category;
import com.example.FoodOrderApp.category.repository.CategoryRepository;
import com.example.FoodOrderApp.exceptions.BadRequestException;
import com.example.FoodOrderApp.exceptions.NotFoundException;
import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.example.FoodOrderApp.menu.entity.Menu;
import com.example.FoodOrderApp.menu.repository.MenuRepository;
import com.example.FoodOrderApp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AWSS3Service awss3Service;


    @Override
    public Response<MenuDTO> createMenu(MenuDTO menuDTO) {
        log.info("Inside createMenu()");
        Category category = categoryRepository.findById(menuDTO.getCategoryId())
                .orElseThrow(()-> new NotFoundException("Category Not Found"));
        String imageUrl = null;

        MultipartFile imageFile = menuDTO.getImageFile();

        if(imageFile == null || imageFile.isEmpty()){
            throw new BadRequestException("Menu image is required");
        }

        String imageName = UUID.randomUUID()+"-"+imageFile.getOriginalFilename();
        URL s3url = awss3Service.uploadFile("menus/"+imageName, imageFile);
        imageUrl = s3url.toString();

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

        return null;
    }

    @Override
    public Response<MenuDTO> getMenuById(Long id) {
        log.info("Inside getMenuById()");

        return null;
    }

    @Override
    public Response<?> deleteMenu(Long id) {
        log.info("Inside deleteMenu()");

        return null;
    }

    @Override
    public Response<List<MenuDTO>> getMenus(Long categoryId, String search) {
        log.info("Inside getMenus()");
        return null;
    }
}
