package com.example.service;

import com.example.dto.FilterResultDTO;
import com.example.dto.profileDTO.*;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileStatus;
import com.example.exceptions.ItemAlreadyExistsException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.ProfileCustomRepository;
import com.example.repository.ProfileRepository;
import com.example.utility.CheckValidationUtility;
import com.example.utility.MD5util;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private CheckValidationUtility checkValidationUtility;
    @Autowired
    private ProfileCustomRepository profileCustomRepository;
    @Autowired
    private AttachService attachService;
    // Create profile by admin
    public ProfileDTO createAdmin(CreateProfileDTO dto, Language language) {
        String validPhone=checkValidationUtility.checkForPhone(dto.getPhone(),language);
        checkValidationUtility.checkForPassword(dto.getPassword());
        isPhoneExists(validPhone);
        isEmailExists(dto.getEmail());
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(validPhone);
        entity.setPassword( MD5util.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
        entity.setPhotoId(dto.getPhotoId());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        ProfileEntity savedEntity = profileRepository.save(entity);
        return toDTO(savedEntity);
    }
    public ProfileDTO createProfile(CreateProfileDTO dto,Language language) {
        String validPhone=checkValidationUtility.checkForPhone(dto.getPhone(),language);
        checkValidationUtility.checkForPassword(dto.getPassword());
        isPhoneExists(validPhone);
        isEmailExists(dto.getEmail());
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(validPhone);
        entity.setPassword( MD5util.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
        entity.setPhotoId(dto.getPhotoId());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        ProfileEntity savedEntity = profileRepository.save(entity);
        return toDTO(savedEntity);
    }
// update profile any
    public ProfileDTO updateProfile(UpdateProfileDTO dto){
        ProfileEntity entity = SpringSecurityUtil.getCurrentUser();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        profileRepository.save(entity);
        return toDTO(entity);
    }

    // update profile admin
    public ProfileDTO updateProfileAdmin(UpdateProfileByAdminDTO dto, String profileId){
        ProfileEntity entity = getProfileById(profileId);
        if(dto.getEmail()!=null && !dto.getEmail().isEmpty()){
            isEmailExists(dto.getEmail());
            entity.setEmail(dto.getEmail());
        }
        if(dto.getPhone()!=null && !dto.getPhone().isEmpty()){
            isPhoneExists(dto.getPhone());
            entity.setPhone(dto.getPhone());
        }
       if(dto.getPassword()!=null && !dto.getPassword().isEmpty()){
           checkValidationUtility.checkForPassword(dto.getPassword());
           entity.setPassword(MD5util.encode(dto.getPassword()));
       }
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setStatus(dto.getStatus());
        entity.setRole(dto.getRole());
        entity.setPhotoId(dto.getPhotoId());
        entity.setVisible(dto.isVisible());
        entity.setPrtId(SpringSecurityUtil.getCurrentUserId());
        profileRepository.save(entity);
        return toDTO(entity);
    }

    // get all profile pagination
    public PageImpl<ProfileDTO> getAllProfilesPagination(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<ProfileEntity> pageObg=profileRepository.findAllByVisibleTrue(pageable);
        return  new PageImpl<>(pageObg.getContent().stream().map(this::toDTO).toList(), pageable, pageObg.getTotalElements());
    }
    // delete profileByAdmin
    public boolean deleteProfile(String id){
        getProfileById(id);
   return profileRepository.deleteProfile(id) > 0;
    }
// filter profile pagination by admin

    public PageImpl<ProfileDTO> filterPagination(ProfileFilterPaginationDTO dto, Integer page, Integer size){
        FilterResultDTO<ProfileEntity> result=profileCustomRepository.profileFilter(dto,page,size);
        Pageable pageable=PageRequest.of(page,size, Sort.by("createdDate").descending());
        return new  PageImpl<>(result.getContentList().stream().map(this::toDTO).toList(), pageable, result.getTotalElements());
    }
    private ProfileEntity getProfileById(String id){
        return profileRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Profile Not found"));
    }

    private void isPhoneExists(String phone){
        boolean isPresent=profileRepository.findAllByPhoneAndStatusAndVisibleTrue(phone,ProfileStatus.ACTIVE).isPresent();
        System.out.println("ISPRESENT "+isPresent);
        if(isPresent)throw new ItemAlreadyExistsException("Phone already exists");
    }
    private void isEmailExists(String email){
        boolean isPresent=profileRepository.findAllByEmailAndStatusAndVisibleTrue(email,ProfileStatus.ACTIVE).isPresent();

        if(isPresent)throw new ItemAlreadyExistsException("Email already exists");
    }
    private ProfileDTO toDTO(ProfileEntity entity){
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        dto.setPhotoId(entity.getPhotoId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVisible(entity.isVisible());
        if(entity.getPrtId()!=null){
            dto.setPrtId(entity.getPrtId());
        }
        return dto;
    }


    public Boolean updatePhoto(String photoId) {
        ProfileEntity entity = SpringSecurityUtil.getCurrentUser();

        if(entity.getPhotoId()!=null){
            if(entity.getPhotoId().equals(photoId)){
                return true;
            }
            attachService.deletePhoto(entity.getPhotoId());
        }
        return profileRepository.updatePhotoId(photoId,entity.getId())>0;

    }
}
