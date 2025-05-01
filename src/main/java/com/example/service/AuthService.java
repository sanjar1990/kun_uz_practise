package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.PhoneVerificationDTO;
import com.example.dto.auth.RegistrationDTO;
import com.example.dto.profileDTO.ProfileDTO;
import com.example.entity.emailHistory.EmailHistoryEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.example.exceptions.AppBadRequestException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.ProfileRepository;
import com.example.utility.JwtUtil;
import com.example.utility.MD5util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public ApiResponseDTO login(AuthDTO dto, Language language) {
        Optional<ProfileEntity> optional=profileRepository.findByPhoneAndPasswordAndVisibleTrue(dto.getPhone(),
                MD5util.encode(dto.getPassword()));
        if(optional.isEmpty()) return new ApiResponseDTO(true,
                messageSource.getMessage("phone.or.password.incorrect",null,new Locale(language.name())));
        ProfileEntity entity=optional.get();
        if(!entity.getStatus().equals(ProfileStatus.ACTIVE))
            return new ApiResponseDTO(true, resourceBundleService.getMessage("please.activate.your.account",language));
        ProfileDTO profile=new ProfileDTO();
        profile.setToken(JwtUtil.encodePhone(entity.getPhone(),entity.getRole()));
        profile.setPhone(entity.getPhone());
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setRole(entity.getRole());
        profile.setId(entity.getId());
        profile.setEmail(entity.getEmail());
        profile.setPhotoId(entity.getPhotoId());
        return new ApiResponseDTO(false,profile);
    }

    public ApiResponseDTO emailRegistration(RegistrationDTO dto) {
        ProfileEntity entity;
        Optional<ProfileEntity> optional=profileRepository.getByEmailAndVisibleTrue(dto.getEmail());
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.getStatus().equals(ProfileStatus.ACTIVE)){
                return new ApiResponseDTO(true,"Email already exists");
            }
            if(entity.getStatus()==ProfileStatus.BLOCKED){
                return new ApiResponseDTO(true,"Email email is blocked");
            }
            if(entity.getStatus()==ProfileStatus.REGISTRATION){
               Optional<EmailHistoryEntity> emailOptional= emailHistoryService.getByEmailLast(dto.getEmail());
               if(emailOptional.isPresent()){
                   EmailHistoryEntity e=emailOptional.get();

                   if(e.getCreatedDate().plusMinutes(3).isAfter(LocalDateTime.now())){
                       System.out.println("TEST");
                       return new ApiResponseDTO(true,"Email already send");
                   }
               }
            }
        }else{
            entity=new ProfileEntity();
            entity.setName(dto.getName());
            entity.setSurname(dto.getSurname());
            entity.setPhone(dto.getPhone());
            entity.setEmail(dto.getEmail());
            entity.setPassword(MD5util.encode(dto.getPassword()));
            entity.setStatus(ProfileStatus.REGISTRATION);
            entity.setRole(ProfileRole.ROLE_USER);
            entity.setVisible(Boolean.TRUE);
            profileRepository.save(entity);
        }

        mailSenderService.sendEmailVerification(entity.getEmail(),entity.getName(),entity.getId());
        return new ApiResponseDTO(false,"Verification mail sent");
    }
    public ApiResponseDTO phoneRegistration(RegistrationDTO dto) {
        ProfileEntity entity;
        Optional<ProfileEntity> optional=profileRepository.getByPhoneAndVisibleTrue(dto.getPhone());
        if(optional.isPresent()){
            entity=optional.get();
            if(entity.getStatus().equals(ProfileStatus.ACTIVE)){
                return new ApiResponseDTO(true,"Phone already exists");
            }
            if(entity.getStatus()==ProfileStatus.BLOCKED){
                return new ApiResponseDTO(true,"Phone is blocked");
            }
        }else{
            entity=new ProfileEntity();
            entity.setName(dto.getName());
            entity.setSurname(dto.getSurname());
            entity.setPhone(dto.getPhone());
            entity.setEmail(dto.getEmail());
            entity.setPassword(MD5util.encode(dto.getPassword()));
            entity.setStatus(ProfileStatus.REGISTRATION);
            entity.setRole(ProfileRole.ROLE_USER);
            entity.setVisible(Boolean.TRUE);
            profileRepository.save(entity);
        }
        smsSenderService.sendSmsVerification(entity.getPhone());
        return new ApiResponseDTO(false,"Sms sent");
    }


    public ApiResponseDTO emailVerification(String jwt) {
        String id=JwtUtil.decodeEmail(jwt);
        ProfileEntity entity=profileRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Email not found"));
        if(!entity.getStatus().equals(ProfileStatus.REGISTRATION))throw new AppBadRequestException("Your account is blocked!");
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        ProfileDTO profile=new ProfileDTO();
        profile.setName(entity.getName());
        profile.setSurname(entity.getSurname());
        profile.setPhone(entity.getPhone());
        profile.setEmail(entity.getEmail());
        if(entity.getPhotoId()!=null){
            profile.setImageUrl(attachService.getAttachUrl(entity.getPhotoId()));
        }
        return new ApiResponseDTO(false,profile);
    }

    public ApiResponseDTO phoneVerification(PhoneVerificationDTO dto) {
        Optional<ProfileEntity> entity=profileRepository.findAllByPhoneAndVisibleTrue(dto.getPhone());
        if(entity.isEmpty()){
            return new ApiResponseDTO(true,"Phone not found");
        }else{
            ProfileEntity profile=entity.get();
            if(profile.getStatus().equals(ProfileStatus.BLOCKED)){
                return new ApiResponseDTO(true,"Phone is blocked");
            }else if(profile.getStatus()==ProfileStatus.ACTIVE){
                return new ApiResponseDTO(true,"Your Account is verified");
            } else {
                boolean result=smsHistoryService.checkSms(dto);
                if(result){
                    profile.setStatus(ProfileStatus.ACTIVE);
                    profileRepository.save(profile);
                    ProfileDTO profileDTO=new ProfileDTO();
                    profileDTO.setName(profile.getName());
                    profileDTO.setSurname(profile.getSurname());
                    profileDTO.setPhone(profile.getPhone());
                    profileDTO.setEmail(profile.getEmail());
                    if(profile.getPhotoId()!=null){
                        profileDTO.setImageUrl(attachService.getAttachUrl(profile.getPhotoId()));
                    }
                    return new ApiResponseDTO(false,profileDTO);
                }
            }
        }
        return new ApiResponseDTO(false,"Phone not found");
    }
}
