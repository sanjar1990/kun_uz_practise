package com.example.repository;

import com.example.entity.category.CategoryEntity;
import com.example.entity.region_entity.RegionEntity;
import com.example.mapper.LangMapperDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    boolean existsByNameEnAndNameUzAndNameRuAndVisibleTrue(String nameEn, String nameUz, String nameRu);
    boolean existsByOrderNumAndVisibleTrue(Integer orderNum);
    @Transactional
    @Modifying
    @Query("update CategoryEntity set visible=false, prtId=:prtId where id=id")
    int deleteCategory(@Param("id") Integer id, @Param("prtId")String prtId);

    List<CategoryEntity> findAllByVisibleTrue();
    @Query(value = "select id, order_num as orderNumber, " +
            "case :lang " +
            "when 'uz' then name_uz " +
            "when 'ru' then name_ru " +
            "when 'en' then name_en " +
            "else name_uz " +
            "end as name " +
            "from category order by order_num asc ", nativeQuery = true)
    List<LangMapperDTO>getByLang(@Param("lang")String lang);
}
