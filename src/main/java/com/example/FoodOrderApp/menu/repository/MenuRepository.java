package com.example.FoodOrderApp.menu.repository;

import com.example.FoodOrderApp.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuRepository extends JpaRepository <Menu, Long>, JpaSpecificationExecutor<Menu> {

}
