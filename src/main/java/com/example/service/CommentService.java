package com.example.service;

import com.example.dto.FilterResultDTO;
import com.example.dto.article.ArticleDTO;
import com.example.dto.comment.CommentDTO;
import com.example.dto.comment.CreateCommentDTO;
import com.example.dto.comment.FilterCommentDTO;
import com.example.dto.comment.UpdateCommentDTO;
import com.example.dto.profileDTO.ProfileDTO;
import com.example.entity.comment.CommentEntity;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.ProfileRole;
import com.example.exceptions.AppBadRequestException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentCustomRepository;
import com.example.repository.CommentRepository;
import com.example.utility.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentCustomRepository commentCustomRepository;
    public CommentDTO createComment(CreateCommentDTO commentDTO) {
        if(!checkArticle(commentDTO.getArticleId())) throw new ItemNotFoundException("Article not found");
        CommentEntity entity = new CommentEntity();
        entity.setArticleId(commentDTO.getArticleId());
        entity.setContent(commentDTO.getContent());
        entity.setProfileId(SpringSecurityUtil.getCurrentUser().getId());
        entity.setVisible(Boolean.TRUE);
        entity.setReplyId(commentDTO.getReplyId());
        commentRepository.save(entity);
        return toShortDto(entity);
    }


    public CommentDTO updateComment(UpdateCommentDTO dto,String commentId) {
        CommentEntity entity = getById(commentId);
        String profileId = SpringSecurityUtil.getCurrentUser().getId();
        if(!entity.getProfileId().equals(profileId))throw new AppBadRequestException("Profile id not match");
        entity.setContent(dto.getContent());
        entity.setUpdatedDate(LocalDateTime.now());
        commentRepository.save(entity);
        return toShortDto(entity);
    }


    public Boolean deleteComment(String id) {
        ProfileEntity profile= SpringSecurityUtil.getCurrentUser();
        CommentEntity comment = getById(id);
        if(comment.getProfileId().equals(profile.getId())|| profile.getRole().equals(ProfileRole.ROLE_ADMIN)){
         return commentRepository.deleteComment(id)>0;
        }
            throw new AppBadRequestException("You can't delete comment");

    }
    private CommentEntity getById(String id) {
        return commentRepository.findByIdAndVisibleTrue(id).orElseThrow(()->new  ItemNotFoundException("comment not find"));
    }
    private CommentDTO toFullDto(CommentEntity entity) {
        CommentDTO dto=new CommentDTO();
        ArticleDTO article= new ArticleDTO();
        article.setId(entity.getArticleId());
        article.setTitle(entity.getArticle().getTitle());
        dto.setArticle(article);
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setVisible(entity.isVisible());
        ProfileDTO profile=new ProfileDTO();
        profile.setId(entity.getProfileId());
        profile.setName(entity.getProfile().getName());
        profile.setSurname(entity.getProfile().getSurname());
        dto.setProfile(profile);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }
    private CommentDTO toShortDto(CommentEntity entity) {
        CommentDTO dto=new CommentDTO();
        dto.setArticleId(entity.getArticleId());
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setVisible(entity.isVisible());
        dto.setProfileId(entity.getProfileId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        return dto;
    }
    private Boolean checkArticle(String articleId) {
        return articleRepository.existsByIdAndVisibleTrue(articleId);}

    public PageImpl<CommentDTO> getByArticleId(String articleId, int page, Integer size) {
        Pageable pageable= PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<CommentEntity> pageObj=commentRepository.getByArticleIdAndVisibleTrue(articleId, pageable);
        List<CommentDTO> content=pageObj.getContent().stream().map(this::toFullDto).toList();
        return new PageImpl<>(content,pageable,pageObj.getTotalElements());
    }

    public PageImpl<CommentDTO> getAllPagination(int page, Integer size) {
        Pageable pageable= PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<CommentEntity> pageObj=commentRepository.getByVisibleTrue(pageable);
        List<CommentDTO> content=pageObj.getContent().stream().map(this::toFullDto).toList();
        return new PageImpl<>(content,pageable,pageObj.getTotalElements());
    }

    public List<CommentDTO> getReply(String commentId) {
        return commentRepository.getAllByReplyIdAndVisibleTrue(commentId).stream().map(this::toFullDto).toList();
    }

    public PageImpl<CommentDTO> getFilterPagination(int page, Integer size, FilterCommentDTO dto) {
        FilterResultDTO<CommentEntity> result=commentCustomRepository.commentFilter(dto,page,size);
        Pageable pageable=PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<CommentDTO> dtoList=result.getContentList().stream().map(this::toFullDto).toList();
        return new PageImpl<>(dtoList,pageable,result.getTotalElements());
    }
}
