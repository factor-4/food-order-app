package com.example.FoodOrderApp.category.repository;

import com.example.FoodOrderApp.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
