package com.example.entity.tag;

import com.example.entity.BaseStringEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tag")
public class TagEntity extends BaseStringEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "prt_id")
    private String prtId;
}
