package com.example.entity.article_tag;

import com.example.entity.BaseStringEntity;
import com.example.entity.article.ArticleEntity;
import com.example.entity.tag.TagEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "article_tag")
public class ArticleTagEntity extends BaseStringEntity {
    @Column(name = "tag_id")
    private String tagId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id",insertable = false, updatable = false)
    private TagEntity tag;
    @Column(name = "article_id")
    private String articleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",insertable = false, updatable = false)
    private ArticleEntity article;
}
