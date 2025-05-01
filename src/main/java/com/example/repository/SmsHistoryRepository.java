package com.example.repository;

import com.example.entity.smsHistory.SmsHistoryEntity;
import com.example.enums.SmsStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.*;
import java.time.LocalDateTime;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,String>,
        PagingAndSortingRepository<SmsHistoryEntity, String> {
    Long countByPhoneAndCreatedDateBetween(String phone, LocalDateTime from, LocalDateTime to);
    List<SmsHistoryEntity>getByPhoneAndVisibleTrue(String phone);
    Page<SmsHistoryEntity> getAllByCreatedDateBetweenAndVisibleTrue(LocalDateTime from, LocalDateTime to, Pageable pageable);
    Optional<SmsHistoryEntity>findTopByPhoneAndVisibleTrueOrderByCreatedDateDesc(String phone);
    @Transactional
    @Modifying
    @Query("update SmsHistoryEntity set status=:status where phone=:phone")
    void updateSmsStatus(@Param("status") SmsStatus status, @Param("phone") String phone);
}
