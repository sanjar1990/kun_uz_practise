package com.example.entity.article;

import com.example.entity.BaseStringEntity;
import com.example.entity.article_tag.ArticleTagEntity;
import com.example.entity.article_types.ArticleTypesEntity;
import com.example.entity.attach.AttachEntity;
import com.example.entity.category.CategoryEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.entity.region_entity.RegionEntity;
import com.example.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseStringEntity {
    @Column(columnDefinition = "Text")
    private String title;
    @Column(columnDefinition = "Text")
    private String description;
    @Column(columnDefinition = "Text")
    private String content;
    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CategoryEntity category;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;
    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="region_id", insertable = false, updatable = false)
    private RegionEntity region;
    @Column(name = "moderator_id")
    private String moderatorId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;
    @Column(name = "publisher_id")
    private String publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;
    @Column
    @Enumerated(EnumType.STRING)
    private ArticleStatus status=ArticleStatus.CREATED;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "shared_count")
    private Integer sharedCount;
    @OneToMany(mappedBy = "article")
    private List<ArticleTypesEntity> articleTypeList;
    @OneToMany(mappedBy = "article")
    private List<ArticleTagEntity> articleTagEntityList;
    @Column(name = "like_count")
    private Integer likeCount=0;
    @Column(name = "dislike_count")
    private Integer dislikeCount=0;

}
