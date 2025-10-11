package com.example.FoodOrderApp.menu.services;

import com.example.FoodOrderApp.menu.dtos.MenuDTO;
import com.example.FoodOrderApp.response.Response;

import java.util.List;

public interface MenuService  {
    Response <MenuDTO> createMenu(MenuDTO menuDTO);
    Response <MenuDTO> updateMenu(MenuDTO menuDTO);
    Response <MenuDTO> getMenuById(Long id);
    Response <?> deleteMenu(Long id);
    Response <List<MenuDTO> > getMenus(Long categoryId, String search);


}
