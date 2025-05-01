package com.example.service;

import com.example.dto.auth.PhoneVerificationDTO;
import com.example.dto.sms.SmsHistoryDTO;
import com.example.entity.smsHistory.SmsHistoryEntity;
import com.example.enums.SmsStatus;
import com.example.enums.SmsType;
import com.example.exceptions.AppBadRequestException;
import com.example.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;
    public Long getLimitCountLastTwoMinutes(String phone) {
        return smsHistoryRepository.countByPhoneAndCreatedDateBetween(phone,
                LocalDateTime.now().minusMinutes(2), LocalDateTime.now());
    }

    public void save(String phone, String smsCode, String message, SmsType smsType) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhone(phone);
        entity.setSmsCode(smsCode);
        entity.setSmsType(smsType);
        entity.setSmsText(message);
        entity.setStatus(SmsStatus.SEND);
        entity.setVisible(Boolean.TRUE);
        smsHistoryRepository.save(entity);
    }

    public List<SmsHistoryDTO> getByPhone(String phone) {
        return smsHistoryRepository.getByPhoneAndVisibleTrue(phone).stream().map(this::toDto).toList();
    }
    private SmsHistoryDTO toDto(SmsHistoryEntity entity) {
        SmsHistoryDTO dto = new SmsHistoryDTO();
        dto.setPhone(entity.getPhone());
        dto.setSmsCode(entity.getSmsCode());
        dto.setSmsType(entity.getSmsType());
        dto.setSmsText(entity.getSmsText());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PageImpl<SmsHistoryDTO> getByDate(int page, Integer size, LocalDate from, LocalDate to) {
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<SmsHistoryEntity> pageObj=smsHistoryRepository.getAllByCreatedDateBetweenAndVisibleTrue(from.atStartOfDay(),
                LocalDateTime.of(to, LocalTime.MAX),pageable);
       List<SmsHistoryDTO>dtoList=pageObj.getContent().stream().map(this::toDto).toList();
        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }

    public boolean checkSms(PhoneVerificationDTO dto) {
        Optional<SmsHistoryEntity> optional=smsHistoryRepository
                .findTopByPhoneAndVisibleTrueOrderByCreatedDateDesc(dto.getPhone());
        if(optional.isEmpty()){
            throw  new AppBadRequestException("Sms Code Wrong");
        } else {
           SmsHistoryEntity entity=optional.get();
           if(entity.getStatus().equals(SmsStatus.USED) || entity.getStatus().equals(SmsStatus.USED_WITH_TIMEOUT)){
               throw  new AppBadRequestException("Sms code expired");
           }
           if(entity.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())) {
                smsHistoryRepository.updateSmsStatus(SmsStatus.USED_WITH_TIMEOUT,dto.getPhone());
               throw  new AppBadRequestException("Sms code expired");
           }
            if(!entity.getSmsCode().equals(dto.getCode())){
                throw  new AppBadRequestException("Wrong sms code");
            }
       }
        smsHistoryRepository.updateSmsStatus(SmsStatus.USED,dto.getPhone());
        return true;
    }
}
