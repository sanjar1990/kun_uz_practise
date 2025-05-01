package com.example.repository;

import com.example.entity.attach.AttachEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AttachRepository extends CrudRepository<AttachEntity,String > {


}
