package com.example.FoodOrderApp.role.services;

import com.example.FoodOrderApp.response.Response;
import com.example.FoodOrderApp.role.dtos.RoleDTO;

import java.util.List;

public interface RoleService  {

    Response<RoleDTO> createRole(RoleDTO roleDTO);

    Response<RoleDTO> updateRole(RoleDTO roleDTO);

    Response<List <RoleDTO> > getAllRoles();

    Response<?> deleteROle(Long id);


}
