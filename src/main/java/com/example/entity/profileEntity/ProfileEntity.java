package com.example.entity.profileEntity;

import com.example.entity.BaseStringEntity;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseStringEntity {

    @Column(name = "name", length = 25)
    private String name;
    @Column(name = "surname", length = 25)
    private String surname;
    @Column(name = "email", length = 25,  nullable = false)
    private String email;
    @Column(name = "phone", length = 25,  nullable = false)
    private String phone;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status=ProfileStatus.NOT_ACTIVE;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "prt_id")
    private String prtId;
    @ManyToOne
    @JoinColumn(name = "prt_id",updatable = false,insertable = false)
    private ProfileEntity profile;

}
