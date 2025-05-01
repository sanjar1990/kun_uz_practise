package com.example.repository;
import com.example.dto.article.ArticleFullInfoMapper;
import com.example.dto.article.ArticleShortInfoDTO;
import com.example.entity.article.ArticleEntity;
import com.example.enums.Language;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends ListCrudRepository<ArticleEntity,String>,CrudRepository<ArticleEntity,String>, PagingAndSortingRepository<ArticleEntity,String> {
    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible=false where id=:id")
    Integer deleteArticleById(@Param("id") String id);
    @Transactional
    @Modifying
    @Query("update ArticleEntity set status=PUBLISHED, publishedDate= :date, publisherId=:prtId where id=:id and visible=true ")
    int updateArticleStatus(@Param("id") String id, @Param("date") LocalDateTime publishedDate, @Param("prtId") String prtId);
    @Query("from ArticleEntity as a inner join  a.articleTypeList as at where a.status=PUBLISHED and a.visible=true " +
            "and at.articleTypeId=:typeId order by a.createdDate desc limit :limit")
    List<ArticleEntity> getLastArticle(@Param("typeId") Integer typeId, @Param("limit") Integer limit);

    @Query("from ArticleEntity where id not in :list and visible=true and status=PUBLISHED" +
            " order by createdDate desc limit 8 ")
    List<ArticleEntity> getLastEightArticle(@Param("list") List<String> listId);
    @Query(value = " select  a.id, a.title, a.description, a.content,a.shared_count, a.view_count,a.published_date,\n" +
            "             a.attach_id, \n" +
            "            case :lang \n" +
            "            when 'uz' then c.name_uz\n" +
            "            when 'ru' then c.name_ru\n" +
            "            when 'en' then c.name_en\n" +
            "            else c.name_uz\n" +
            "            end as categoryName, \n" +
            "            c.order_num as categoryOrder," +
            "            c.id as categoryId,\n" +
            "            case :lang \n" +
            "            when 'uz' then r.name_uz \n" +
            "            when 'ru' then r.name_ru \n" +
            "            when 'en' then r.name_en\n" +
            "            else r.name_uz\n" +
            "            end as regionName,\n" +
            "            r.order_num as regionOrder, r.id as regionId \n" +
            "            from article as a inner join category as c on a.category_id=c.id\n" +
            "            inner join region as r on a.region_id=r.id\n" +
            "            where a.id=:id and a.visible=true and a.status='PUBLISHED'", nativeQuery = true)
    ArticleFullInfoMapper getByIdAndLang(@Param("id") String id,@Param("lang") String lang);

    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description,a.attachId,a.publishedDate) " +
            "from ArticleEntity as a" +
            " inner join a.articleTypeList as at " +
            "where at.articleTypeId=?2 and a.id!=?1 and a.visible=true and at.visible=true and a.status=PUBLISHED " +
            "order by a.createdDate desc limit 4")
    List<ArticleShortInfoDTO> getLastFourArticle(String articleId, Integer typeId);

    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a where a.visible=true and a.status=PUBLISHED" +
            " order by a.viewCount desc limit 4")
    List<ArticleShortInfoDTO> getMostViewed();

    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a inner join a.articleTagEntityList as at" +
            " where  at.tagId=?1 and a.visible=true and a.status=PUBLISHED" +
            " order by a.viewCount desc limit 4")
    List<ArticleShortInfoDTO> getByTag(String tagId);
    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a inner join a.articleTypeList as at" +
            " where  at.articleTypeId=?1 and a.regionId=?2 and a.visible=true and a.status=PUBLISHED" +
            " order by a.viewCount desc limit 5")
    List<ArticleShortInfoDTO> getByTypeAndRegionId(Integer typeId, Integer regId);
    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a where a.regionId=?1" +
            " and a.visible=true and a.status=PUBLISHED")
    Page<ArticleShortInfoDTO>getByRegionId(Integer regionId, Pageable pageable);
    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a where a.categoryId=?1 " +
            "and a.visible=true and a.status=PUBLISHED" +
            " order by a.viewCount desc limit 5")
    List<ArticleShortInfoDTO> getByCategoryId(Integer categoryId);
    @Query("select new com.example.dto.article.ArticleShortInfoDTO(a.id,a.title,a.description," +
            "a.attachId,a.publishedDate)from ArticleEntity  as a where a.categoryId=?1" +
            " and a.visible=true and a.status=PUBLISHED")
    Page<ArticleShortInfoDTO> getByCategoryPagination(Integer id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set viewCount=viewCount+1 where id=?1")
    void increaseViewCount(String articleId);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set sharedCount=sharedCount+1 where id=?1")
    int increaseShareCount(String articleId);

    Boolean existsByIdAndVisibleTrue(String articleId);
}
