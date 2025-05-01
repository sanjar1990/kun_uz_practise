package com.example.repository;

import com.example.entity.emailHistory.EmailHistoryEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,String>,
        PagingAndSortingRepository<EmailHistoryEntity, String> {
    List<EmailHistoryEntity> getByEmailAndVisibleTrueOrderByCreatedDateDesc(String email);
    @Query("from EmailHistoryEntity where email=?1 and visible=true order by createdDate desc limit 1")
    Optional<EmailHistoryEntity> getByEmailLast(String email);

    Page<EmailHistoryEntity> getByCreatedDateBetween(LocalDateTime from,
                                                     LocalDateTime to, Pageable pageable);
}
