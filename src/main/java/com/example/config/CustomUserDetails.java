package com.example.config;

import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
@Setter
@Getter
public class CustomUserDetails implements UserDetails {
    private final ProfileEntity profile;
    public CustomUserDetails(ProfileEntity profile) {
        this.profile = profile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority>list=new LinkedList<>();
        list.add(new SimpleGrantedAuthority(profile.getRole().name()));
        return list;
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return profile.isVisible();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return profile.getStatus().equals(ProfileStatus.ACTIVE);
    }
}
