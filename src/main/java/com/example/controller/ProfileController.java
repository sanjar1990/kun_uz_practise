package com.example.controller;

import com.example.dto.profileDTO.*;
import com.example.enums.Language;
import com.example.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/public/admin")
    public ResponseEntity<ProfileDTO> createForAdmin(@RequestBody CreateProfileDTO dto,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language
    ){
        return ResponseEntity.ok(profileService.createAdmin(dto,language));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody CreateProfileDTO dto,
                                                    @RequestHeader(value = "Accept-Language", defaultValue = "uz")Language language){
       return ResponseEntity.ok(profileService.createProfile(dto,language));
    }
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping()
    public ResponseEntity<ProfileDTO>updateProfile(@RequestBody UpdateProfileDTO dto){

        return ResponseEntity.ok(profileService.updateProfile(dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/updateByAdmin/{id}")
    public ResponseEntity<ProfileDTO>updateProfileByAdmin(@PathVariable(value = "id")String profileId,
                                                          @RequestBody UpdateProfileByAdminDTO dto){
        return ResponseEntity.ok(profileService.updateProfileAdmin(dto, profileId));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/profileAllPagination")
    public  ResponseEntity<?> getAllProfilePagination(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                      @RequestParam(value="size", defaultValue = "10")Integer size ){
        return ResponseEntity.ok(profileService.getAllProfilesPagination(page-1,size));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<PageImpl<ProfileDTO>> filterProfile(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "30") Integer size,
                                                              @RequestBody ProfileFilterPaginationDTO dto){
        return ResponseEntity.ok(profileService.filterPagination(dto,page-1,size));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProfile(@PathVariable(value = "id")String id){
        return ResponseEntity.ok(profileService.deleteProfile(id));
    }

    // update phot
//    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/updatePhoto/{photoId}")
    public ResponseEntity<Boolean> updatePhoto(@PathVariable("photoId") String photoId){
        return ResponseEntity.ok(profileService.updatePhoto(photoId));
    }
}
