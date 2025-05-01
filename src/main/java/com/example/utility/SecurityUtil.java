package com.example.utility;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exceptions.AppForbiddenException;
import com.example.exceptions.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String token) {
        if(token.startsWith("Bearer ")) {
            token = token.substring(7);
            return JwtUtil.decode(token);
        }
        throw new UnAuthorizedException("Unauthorized");
    }
    public static JwtDTO hasRole(String token, ProfileRole... profileRole){

        JwtDTO jwtDTO=getJwtDTO(token);
        if(profileRole==null) return jwtDTO;
        boolean result=Arrays.stream(profileRole).anyMatch(s->s.equals(jwtDTO.getRole()));
        if(!result) throw new AppForbiddenException("Forbidden");
        return jwtDTO;
    }
    public static JwtDTO hasRole(HttpServletRequest request, ProfileRole... profileRole){
    String id=request.getAttribute("id").toString();
    ProfileRole role=ProfileRole.valueOf(request.getAttribute("role").toString());
        JwtDTO dto=new JwtDTO(id,role);
    if(profileRole==null) return dto;
    boolean result= Arrays.asList(profileRole).contains(role);
    if(!result) throw new AppForbiddenException("Forbidden");
    return dto;
    }
}
