package com.example.service;

import com.example.dto.region_dto.CreateRegionDTO;
import com.example.dto.region_dto.RegionDTO;
import com.example.entity.region_entity.RegionEntity;
import com.example.enums.Language;
import com.example.exceptions.ItemAlreadyExistsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.RegionRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class RegionService {
@Autowired
    private RegionRepository regionRepository;

// Create region
    public RegionDTO createRegion(CreateRegionDTO dto) {
        checkRegion(dto);
        RegionEntity entity= toEntity(dto);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        regionRepository.save(entity);
        return toDto(entity);
    }

    // update region
    public RegionDTO updateRegion(CreateRegionDTO dto, Integer id) {
//        checkRegion(dto);
        RegionEntity entity=regionRepository.findById(id)
                .orElseThrow(()->new ItemNotFoundException("Region not found"));
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setOrderNum(dto.getOrderNum());
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        regionRepository.save(entity);
        return toDto(entity);
    }

    // Delete Region
    public boolean deleteRegion(Integer id) {
        return regionRepository.deleteRegion(id,SpringSecurityUtil.getCurrentUserId())>0;
    }

    // Get region list by admin
    public List<RegionDTO> getAllRegions() {
        List<RegionDTO> list=new LinkedList<>();
        regionRepository.findAll().forEach(s->{
            list.add(toDto(s));
        });
        return list;
    }

    private void checkRegion(CreateRegionDTO dto){
        if(regionRepository.existsByNameEnAndNameUzAndNameRuAndVisibleTrue(
                dto.getNameEn(), dto.getNameUz(), dto.getNameRu())) throw new ItemAlreadyExistsException("Name already exists");
        if(regionRepository.existsByOrderNumAndVisibleTrue(
                dto.getOrderNum())) throw new ItemAlreadyExistsException("Order number already exists");
    }
    private RegionEntity toEntity(CreateRegionDTO dto){
        RegionEntity entity=new RegionEntity();
        entity.setNameEn(dto.getNameEn());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setOrderNum(dto.getOrderNum());
        entity.setVisible(Boolean.TRUE);
        return entity;
    }
    private RegionDTO toDto(RegionEntity entity) {
        RegionDTO dto=new RegionDTO();
        dto.setNameEn(entity.getNameEn());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setOrderNum(entity.getOrderNum());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());
        dto.setVisible(entity.getVisible());
        return dto;
    }


    public List<RegionDTO> getByLangOne(Language language) {
    List<RegionEntity> list=regionRepository.findAllByVisibleTrue();
    List<RegionDTO> dtoList=new LinkedList<>();
    list.forEach(s->{
        RegionDTO dto=new RegionDTO();
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
    public List<RegionDTO> getByLangTwo(Language language) {
       List<RegionDTO> list=new LinkedList<>();
        regionRepository.getByLang(language.name()).forEach(s->{
            RegionDTO dto=new RegionDTO();
            dto.setId(s.getId());
            dto.setOrderNum(s.getOrderNumber());
            dto.setName(s.getName());
            list.add(dto);
        });
        return   list;
    }
}
