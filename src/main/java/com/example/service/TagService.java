package com.example.service;

import com.example.dto.tag.TagDTO;
import com.example.entity.tag.TagEntity;
import com.example.exceptions.ItemAlreadyExistsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.TagRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(String name) {
        checkByName(name);
        TagEntity entity = new TagEntity();
        entity.setName(name);
        entity.setVisible(Boolean.TRUE);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        tagRepository.save(entity);
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setTagName(entity.getName());
        return dto;
    }
    private void checkByName(String name) {
        if(tagRepository.existsByNameAndVisibleTrue(name))throw  new ItemAlreadyExistsException("Tag already exists");
    }

    public Boolean deleteById(String id) {
        return tagRepository.deleteById(id,SpringSecurityUtil.getCurrentUserId())>0;
    }

    public TagDTO getById(String id) {
        return toDto(tagRepository.getByIdAndVisibleTrue(id)
                .orElseThrow(()->new ItemNotFoundException("Tag not found")));
    }
    private TagDTO toDto(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setTagName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<TagDTO> getAll() {
       return tagRepository.getAllByVisibleTrue().stream().map(this::toDto).toList();
    }
}
