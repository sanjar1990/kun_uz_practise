package com.example.config;

import com.example.entity.profileEntity.ProfileEntity;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
   @Autowired
   private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);
        Optional<ProfileEntity> optional=profileRepository.findByPhone(username);
        if(optional.isEmpty()) throw  new UsernameNotFoundException(username);
        ProfileEntity profileEntity=optional.get();
        return new CustomUserDetails(profileEntity);
    }
}
