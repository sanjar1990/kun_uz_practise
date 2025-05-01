package com.example.repository;

import com.example.entity.articleTypeEntity.ArticleTypeEntity;
import com.example.enums.Language;
import com.example.mapper.LangMapperDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer>,
        PagingAndSortingRepository<ArticleTypeEntity, Integer> {

   Boolean existsByVisibleTrueAndNameEnAndNameUzAndNameRu( String nameEn, String nameUz,String nameRu);

    Boolean existsByOrderNumAndVisibleTrue(Integer orderNum);

    @Transactional
    @Modifying
    @Query("update ArticleTypeEntity  set visible=false, prtId=:prtId where id=:id")
    int deleteArticleType(@Param("id") Integer id, @Param("prtId") String prtId);
    Page<ArticleTypeEntity> findAllByVisibleTrue(Pageable pageable);

    @Query(value = "select at.id, at.order_num as orderNumber," +
            "CASE :lang " +
            "WHEN 'uz' THEN name_uz " +
            "when 'ru' then name_ru " +
            "when 'en' then name_en " +
            "else name_uz " +
            "end as name " +
            "from article_type as at order by order_num desc", nativeQuery = true)
     List<LangMapperDTO> getByLang(@Param("lang") String lang);
}
