package com.example.repository;

import com.example.dto.FilterResultDTO;
import com.example.dto.profileDTO.ProfileFilterPaginationDTO;
import com.example.entity.profileEntity.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProfileCustomRepository {
@Autowired
    private EntityManager entityManager;

public FilterResultDTO<ProfileEntity>profileFilter(ProfileFilterPaginationDTO dto, Integer page, Integer size){
    StringBuilder selectBuilder = new StringBuilder("select s from ProfileEntity as s");
    StringBuilder countBuilder = new StringBuilder("select count(s) from ProfileEntity as s");
    StringBuilder stringBuilder=new StringBuilder(" where s.visible=true");

    Map<String , Object> params = new HashMap<>();
    if(dto.getName()!=null && !dto.getName().isEmpty()){
        stringBuilder.append(" and lower(s.name) like :name");
        params.put("name","%"+dto.getName().toLowerCase()+"%");
    }
    if(dto.getSurname()!=null && !dto.getSurname().isEmpty()){
        stringBuilder.append(" and lower(s.surname) like :surname ");
        params.put("surname","%"+dto.getSurname().toLowerCase()+"%");
    }
    if(dto.getPhone()!=null && !dto.getPhone().isEmpty()){
        stringBuilder.append(" and s.phone like :phone ");
        params.put("phone",dto.getPhone());
    }
    if(dto.getRole()!=null){
        stringBuilder.append(" and s.role like :role ");
        params.put("role",dto.getRole());
    }
    if(dto.getFrom()!=null){
        stringBuilder.append(" and s.createdDate >=:from");
        params.put("from", dto.getFrom().atStartOfDay());
    }
    if(dto.getTo()!=null){
        stringBuilder.append(" and s.createdDate <=:to");
        params.put("to", LocalDateTime.of(dto.getTo(), LocalTime.MAX));
    }
    selectBuilder.append(stringBuilder);
    countBuilder.append(stringBuilder);
    selectBuilder.append(" order by s.createdDate desc");

    System.out.println(selectBuilder);
    System.out.println(countBuilder);
    Query selectQuery=entityManager.createQuery(selectBuilder.toString());
   selectQuery.setMaxResults(size);
    selectQuery.setFirstResult(page*size);
    Query countQuery=entityManager.createQuery(countBuilder.toString());
    for (Map.Entry<String,Object> param : params.entrySet()) {
        selectQuery.setParameter(param.getKey(),param.getValue());
        countQuery.setParameter(param.getKey(),param.getValue());
    }
    List<ProfileEntity> list=selectQuery.getResultList();
    Long totalCount= (Long) countQuery.getSingleResult();
return new FilterResultDTO<ProfileEntity>(list,totalCount);
}
}
