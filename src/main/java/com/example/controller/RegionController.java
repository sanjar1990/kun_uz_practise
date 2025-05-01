package com.example.controller;

import com.example.config.CustomUserDetails;
import com.example.dto.JwtDTO;
import com.example.dto.region_dto.CreateRegionDTO;
import com.example.dto.region_dto.RegionDTO;
import com.example.entity.profileEntity.ProfileEntity;
import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.service.RegionService;
import com.example.utility.SecurityUtil;
import com.example.utility.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
@Tag(name = "Region api list", description = "this api is for region")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping()
    @Operation(summary = "create region",description = "this api used for region creation")
    public ResponseEntity<RegionDTO> createRegion(@RequestBody CreateRegionDTO dto) {
        return ResponseEntity.ok(regionService.createRegion(dto));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO>updateRegion(@RequestBody CreateRegionDTO dto,
                                                 @PathVariable Integer id) {
        return ResponseEntity.ok(regionService.updateRegion(dto, id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>deleteRegion(@PathVariable Integer id) {
        return ResponseEntity.ok(regionService.deleteRegion(id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }
    @GetMapping("/public/lang")
    public ResponseEntity<List<RegionDTO>> getAllRegionsByLanguage(
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language lang) {
        System.out.println("LANGUAGE: " + lang);
        return ResponseEntity.ok(regionService.getByLangTwo(lang));
    }
}
