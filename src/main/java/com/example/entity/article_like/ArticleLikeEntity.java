package com.example.entity.article_like;

import com.example.entity.BaseStringEntity;
import com.example.entity.article.ArticleEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.ArticleLiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_like")
public class ArticleLikeEntity extends BaseStringEntity {
   @Column(name = "profile_id")
   private String profileId;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "profile_id",insertable = false, updatable = false)
   private ProfileEntity profile;
   @Column(name = "article_id")
   private String articleId;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "article_id",insertable = false, updatable = false)
   private ArticleEntity article;
   @Column(name = "status")
   @Enumerated(EnumType.STRING)
   private ArticleLiceStatus status;
}
