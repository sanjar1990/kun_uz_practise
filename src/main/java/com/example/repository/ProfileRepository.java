package com.example.repository;

import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,String >, PagingAndSortingRepository<ProfileEntity,String> {
    Optional<ProfileEntity>findAllByPhoneAndStatusAndVisibleTrue(String phone, ProfileStatus status);

    Optional<ProfileEntity> findAllByEmailAndStatusAndVisibleTrue(String email, ProfileStatus profileStatus);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=false, status=NOT_ACTIVE where id=?1")
    int deleteProfile(String id);
    Page<ProfileEntity>findAllByVisibleTrue(Pageable pageable);

    Optional<ProfileEntity>findByPhoneAndPasswordAndVisibleTrue(String phone, String password);
    Optional<ProfileEntity> getByPhoneAndVisibleTrue(String phone);
    Optional<ProfileEntity> getByEmailAndVisibleTrue(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set photoId=?1 where id=?2")
    int updatePhotoId(String photoId, String profileId);
    Optional<ProfileEntity>findAllByPhoneAndVisibleTrue(String phone);

    Optional<ProfileEntity> findByPhone(String username);
}
