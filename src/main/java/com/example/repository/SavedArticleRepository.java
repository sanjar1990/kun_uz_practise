package com.example.repository;

import com.example.entity.saved_article.SavedArticleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity,String> {
    Optional<SavedArticleEntity>getByArticleIdAndProfileId(String articleId, String profileId);
    List<SavedArticleEntity>getAllByProfileIdAndVisibleTrue(String profileId);
}
