package com.example.service;

import com.example.dto.category.CategoryDTO;
import com.example.dto.category.CreateCategoryDTO;
import com.example.entity.category.CategoryEntity;
import com.example.enums.Language;
import com.example.exceptions.ItemAlreadyExistsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.CategoryRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    // Create region
    public CategoryDTO createCategory(CreateCategoryDTO dto) {
        checkCategory(dto);
        CategoryEntity entity= toEntity(dto);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        categoryRepository.save(entity);
        return toDto(entity);
    }

    // update category
    public CategoryDTO updateCategory(CreateCategoryDTO dto, Integer id) {
//        checkCategory(dto);
        CategoryEntity entity=categoryRepository.findById(Long.valueOf(id))
                .orElseThrow(()->new ItemNotFoundException("Category not found"));
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setOrderNum(dto.getOrderNum());
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        categoryRepository.save(entity);
        return toDto(entity);
    }

    // Delete Category
    public boolean deleteCategory(Integer id) {
        int n= categoryRepository.deleteCategory(id,SpringSecurityUtil.getCurrentUserId());
        System.out.println("NN"+n);
        return n>0;
    }

    // Get category list by admin
    public List<CategoryDTO> getAllCategorys() {
        List<CategoryDTO> list=new LinkedList<>();
        categoryRepository.findAll().forEach(s->{
            list.add(toDto(s));
        });
        return list;
    }

    private void checkCategory(CreateCategoryDTO dto){
        if(categoryRepository.existsByNameEnAndNameUzAndNameRuAndVisibleTrue(
                dto.getNameEn(), dto.getNameUz(), dto.getNameRu())) throw new ItemAlreadyExistsException("Name already exists");
        if(categoryRepository.existsByOrderNumAndVisibleTrue(
                dto.getOrderNum())) throw new ItemAlreadyExistsException("Order number already exists");
    }
    private CategoryEntity toEntity(CreateCategoryDTO dto){
        CategoryEntity entity=new CategoryEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setOrderNum(dto.getOrderNum());
        entity.setVisible(Boolean.TRUE);
        return entity;
    }
    private CategoryDTO toDto(CategoryEntity entity) {
        CategoryDTO dto=new CategoryDTO();
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setOrderNum(entity.getOrderNum());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setVisible(entity.getVisible());
        return dto;
    }


    public List<CategoryDTO> getByLangOne(Language language) {
        List<CategoryEntity> list=categoryRepository.findAllByVisibleTrue();
        List<CategoryDTO> dtoList=new LinkedList<>();
        list.forEach(s->{
            CategoryDTO dto=new CategoryDTO();
            dto.setId(s.getId());
            dto.setOrderNum(s.getOrderNum());
            switch (language){
                case en -> dto.setName(s.getNameEn());
                case uz -> dto.setName(s.getNameUz());
                case ru -> dto.setName(s.getNameRu());
            }
            dtoList.add(dto);
        });
        return   dtoList;
    }
    // get by lang version two
    public List<CategoryDTO> getByLangTwo(Language language) {
        List<CategoryDTO> list=new LinkedList<>();
        categoryRepository.getByLang(language.name()).forEach(s->{
            CategoryDTO dto=new CategoryDTO();
            dto.setId(s.getId());
            dto.setOrderNum(s.getOrderNumber());
            dto.setName(s.getName());
            list.add(dto);
        });
        return   list;
    }
}
