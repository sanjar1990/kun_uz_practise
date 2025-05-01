package com.example.service;

import com.example.dto.articleTypeDTO.ArticleTypeDTO;
import com.example.dto.articleTypeDTO.CreateArticleTypeDTO;
import com.example.entity.articleTypeEntity.ArticleTypeEntity;
import com.example.enums.Language;
import com.example.exceptions.ItemAlreadyExistsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.mapper.LangMapperDTO;
import com.example.repository.ArticleTypeRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    // Create article type by admin
    public ArticleTypeDTO create(CreateArticleTypeDTO dto) {
        boolean nameExists=articleTypeRepository.existsByVisibleTrueAndNameEnAndNameUzAndNameRu(
                dto.getNameEn(), dto.getNameUz(), dto.getNameRu());
        boolean orderByExists=articleTypeRepository.existsByOrderNumAndVisibleTrue(dto.getOrderNum());
        if(nameExists) throw  new ItemAlreadyExistsException("Name already exists");
        if(orderByExists) throw  new ItemAlreadyExistsException("Order number already exists");
        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameRu(dto.getNameRu());
        entity.setNameUz(dto.getNameUz());
        entity.setOrderNum(dto.getOrderNum());
        entity.setVisible(Boolean.TRUE);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        entity = articleTypeRepository.save(entity);
        return toDto(entity);
    }

    // update article type by admin

    public ArticleTypeDTO updateArticleType(CreateArticleTypeDTO dto, Integer id) {
       ArticleTypeEntity entity=articleTypeRepository.findById(id)
               .orElseThrow(()-> new ItemNotFoundException("article type not found"));
       entity.setNameEn(dto.getNameEn());
       entity.setNameRu(dto.getNameRu());
       entity.setNameUz(dto.getNameUz());
       entity.setOrderNum(dto.getOrderNum());
       entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
       return toDto(entity);
    }

    // Delete by id
    public boolean deleteArticleType(Integer id) {
        return articleTypeRepository.deleteArticleType(id, SpringSecurityUtil.getCurrentUserId())>0;
    }
    //Get all article type pagination

    public PageImpl<ArticleTypeDTO> getAllPagination(Integer page, Integer size) {
        Pageable pageable= PageRequest.of(page,size, Sort.by("orderNum").ascending());
        Page<ArticleTypeEntity> pageObj=articleTypeRepository.findAllByVisibleTrue(pageable);
        return new PageImpl<>(pageObj.getContent().stream().map(this::toDto).toList(), pageable, pageObj.getTotalElements());
    }
    // Get by lang
//    public List<ArticleTypeDTO> getByLang(String lang) {
//
//    }
    private ArticleTypeDTO toDto(ArticleTypeEntity entity) {
        ArticleTypeDTO dto=new ArticleTypeDTO();
        dto.setId(entity.getId());
        dto.setNameEn(entity.getNameEn());
        dto.setNameRu(entity.getNameRu());
        dto.setNameUz(entity.getNameUz());
        dto.setOrderNum(entity.getOrderNum());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }


    public List<ArticleTypeDTO> getByLang(Language lang) {
        List<LangMapperDTO> list=articleTypeRepository.getByLang(lang.name());
        List<ArticleTypeDTO> dtoList=new ArrayList<>();
        list.forEach(e->{
            ArticleTypeDTO dto=new ArticleTypeDTO();
            dto.setId(e.getId());
            dto.setOrderNum(e.getOrderNumber());
            dto.setName(e.getName());
            dtoList.add(dto);
        });
        return dtoList;
    }
}
