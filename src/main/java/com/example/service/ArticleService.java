package com.example.service;

import com.example.dto.FilterResultDTO;
import com.example.dto.article.*;
import com.example.dto.attach.AttachDTO;
import com.example.dto.category.CategoryDTO;
import com.example.dto.region_dto.RegionDTO;
import com.example.entity.article.ArticleEntity;
import com.example.enums.ArticleStatus;
import com.example.enums.Language;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.ArticleCustomRepository;
import com.example.repository.ArticleRepository;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleTypesService articleTypesService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleCustomRepository articleCustomRepository;

    public ArticleDTO createArticle(CreateArticleDTO dto) {
        String prtId=SpringSecurityUtil.getCurrentUserId();
        ArticleEntity entity=new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAttachId(dto.getAttachId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(prtId);
        entity.setViewCount(0);
        entity.setSharedCount(0);
        entity.setVisible(true);
        articleRepository.save(entity);
        articleTypesService.create(entity.getId(), dto.getArticleTypeList());
        articleTagService.create(entity.getId(),dto.getTagList());
        ArticleDTO articleDTO=new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(dto.getTitle());
        articleDTO.setDescription(dto.getDescription());
        articleDTO.setContent(dto.getContent());
        articleDTO.setCategoryId(dto.getCategoryId());
        articleDTO.setAttachId(dto.getAttachId());
        articleDTO.setRegionId(dto.getRegionId());
        articleDTO.setModeratorId(prtId);
        articleDTO.setCreatedDate(entity.getCreatedDate());
        return articleDTO;
    }

    public Boolean updateArticle(String id,CreateArticleDTO dto) {
        String oldAttachId="";
        ArticleEntity entity=getById(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setCategoryId(dto.getCategoryId());
        entity.setRegionId(dto.getRegionId());
        entity.setModeratorId(SpringSecurityUtil.getCurrentUserId());
        articleTypesService.merge(id,dto.getArticleTypeList());
        articleTagService.merge(id,dto.getTagList());
        entity.setStatus(ArticleStatus.CREATED);
        entity.setPublishedDate(null);
        entity.setAttachId(dto.getAttachId());
        articleRepository.save(entity);
        if(oldAttachId.equals(dto.getAttachId())){
            attachService.deletePhoto(entity.getAttachId());
        }
        return true;
    }



    public Boolean delete(String id) {
        int n=articleRepository.deleteArticleById(id);
        return n>0;
    }

    public Boolean updateStatus(String id) {
    return articleRepository.updateArticleStatus(id, LocalDateTime.now(), SpringSecurityUtil.getCurrentUserId())>0;
    }

    public List<ArticleShortInfoDTO> getArticleByType(Integer typeId, Integer limit) {
        return getShortInfo(articleRepository.getLastArticle(typeId, limit));
    }
// 7. Get Last 8  Articles witch id not included in given list.
    public List<ArticleShortInfoDTO> getLastEightArticle(List<String> listId) {
        List<ArticleEntity> entityList=articleRepository.getLastEightArticle(listId);
    return getShortInfo(entityList);
    }

//  8. Get Article By Id And Lang
    @Transactional
    public ArticleFullInfoDTO getByIdAndLang(String id, Language lang) {
        increaseViewCount(id);
        ArticleFullInfoMapper mapper=articleRepository.getByIdAndLang(id, lang.name().toLowerCase());
        if(mapper==null) throw new ItemNotFoundException("Article Not Found");
        ArticleFullInfoDTO articleDTO=new ArticleFullInfoDTO();
        articleDTO.setId(mapper.getId());
        articleDTO.setTitle(mapper.getTitle());
        articleDTO.setDescription(mapper.getDescription());
        articleDTO.setContent(mapper.getContent());
        articleDTO.setSharedCount(mapper.getSharedCount());
        articleDTO.setViewCount(mapper.getViewCount());
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(mapper.getAttachId());
        attachDTO.setUrl(attachService.getAttachUrl(mapper.getAttachId()));
        articleDTO.setAttach(attachDTO);
        articleDTO.setPublishedDate(mapper.getPublishedDate());
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(mapper.getCategoryId());
        categoryDTO.setName(mapper.getCategoryName());
        categoryDTO.setOrderNum(mapper.getCategoryOrder());
        articleDTO.setCategory(categoryDTO);
        RegionDTO regionDTO=new RegionDTO();
        regionDTO.setId(mapper.getRegionId());
        regionDTO.setName(mapper.getRegionName());
        regionDTO.setOrderNum(mapper.getRegionOrder());
        articleDTO.setRegion(regionDTO);
        articleDTO.setTagName(articleTagService.getTagListByArticleId(id));
        return articleDTO;
    }

    //. Get Last 4 Article By Types and except given article id.
    private List<ArticleShortInfoDTO> getShortInfo(List<ArticleEntity> entityList){
        return entityList.stream().map(s->{
            ArticleShortInfoDTO article=new ArticleShortInfoDTO();
            article.setId(s.getId());
            article.setTitle(s.getTitle());
            article.setDescription(s.getDescription());
            article.setImageUrl(attachService.getAttachUrl(s.getAttachId()));
            article.setPublishedDate(s.getPublishedDate());
            return article;
        }).toList();
    }
    private ArticleEntity getById(String id) {
        return articleRepository.findById(id).orElseThrow(()->new ItemNotFoundException("Article Not Found"));
    }

    public List<ArticleShortInfoDTO> getLastFourArticle(String articleId, Integer typeId) {
        List<ArticleShortInfoDTO> articleList=articleRepository.getLastFourArticle(articleId,typeId);
        articleList.forEach(s->{
           attachService.getAttachUrl(s.getAttachId());
           s.setImageUrl(attachService.getAttachUrl(s.getAttachId()));
           s.setAttachId(null);
        });
        return articleList;
    }

    public List<ArticleShortInfoDTO> getMostView() {
      List<ArticleShortInfoDTO> list=articleRepository.getMostViewed();
      list.forEach(a->{
          a.setImageUrl(attachService.getAttachUrl(a.getAttachId()));
      });
      return list;
       }

    public List<ArticleShortInfoDTO> getByTag(String tagId) {
        List<ArticleShortInfoDTO> list=articleRepository.getByTag(tagId);
        list.forEach(a->{
            a.setImageUrl(attachService.getAttachUrl(a.getAttachId()));
        });
        return list;
    }

    public List<ArticleShortInfoDTO> getByTypeAndRegion(Integer typeId, Integer regId) {
        return  toShortInfo(articleRepository.getByTypeAndRegionId(typeId,regId));
    }
    private List<ArticleShortInfoDTO> toShortInfo(List<ArticleShortInfoDTO> entityList) {
        entityList.forEach(s->{
            s.setImageUrl(attachService.getAttachUrl(s.getAttachId()));
            s.setAttachId(null);
        });
        return entityList;
    }

    public PageImpl<ArticleShortInfoDTO> getByRegId(Integer page, Integer size, Integer regId) {
        Pageable pageable= PageRequest.of(page,size, Sort.by("viewCount").descending());
        Page<ArticleShortInfoDTO> pageObj=articleRepository.getByRegionId(regId,pageable);
        toShortInfo(pageObj.getContent());
        return new PageImpl<ArticleShortInfoDTO>(pageObj.getContent(), pageable, pageObj.getTotalElements());
    }

    public List<ArticleShortInfoDTO> getByCategory(Integer categoryId) {
    return toShortInfo(articleRepository.getByCategoryId(categoryId));
    }

    public PageImpl<ArticleShortInfoDTO> getByCategoryPagination(Integer id, Integer page, Integer size) {
    Pageable pageable=PageRequest.of(page,size, Sort.by("viewCount").descending());
        Page<ArticleShortInfoDTO> pageObj=articleRepository.getByCategoryPagination(id,pageable);
        return new PageImpl<ArticleShortInfoDTO>(  toShortInfo(pageObj.getContent()), pageable, pageObj.getTotalElements());
    }
    //     16. Increase Article View Count by Article Id
    private void increaseViewCount(String articleId) {
        articleRepository.increaseViewCount(articleId);
    }

    public Boolean increaseShareCount(String articleId) {
        return articleRepository.increaseShareCount(articleId)>0;
    }

    public PageImpl<ArticleShortInfoDTO> filterPagination(ArticleFilterPaginationDTO dto, int page, Integer size) {
        Pageable pageable=PageRequest.of(page,size, Sort.by("createdDate").descending());
   FilterResultDTO<ArticleShortInfoDTO> filterResultDTO= articleCustomRepository.articleShortInfoFilter(dto,page,size);
    return new PageImpl<>(toShortInfo(filterResultDTO.getContentList()),pageable,filterResultDTO.getTotalElements());
    }
}
