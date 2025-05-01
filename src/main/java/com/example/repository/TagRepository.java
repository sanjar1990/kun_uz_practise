package com.example.repository;

import com.example.entity.tag.TagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity,String> {
    Boolean existsByNameAndVisibleTrue(String name);

    @Transactional
    @Modifying
    @Query("update TagEntity set visible=false, prtId=:prtId where id=:id")
     int deleteById(@Param("id") String id, @Param("prtId") String prtId);
    Optional<TagEntity> getByIdAndVisibleTrue(String id);
    List<TagEntity>getAllByVisibleTrue();
}
