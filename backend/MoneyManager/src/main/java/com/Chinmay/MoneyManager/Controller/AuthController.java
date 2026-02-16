package com.Chinmay.MoneyManager.Controller;

import com.Chinmay.MoneyManager.IO.AuthDTO;
import com.Chinmay.MoneyManager.IO.ErrorResponse;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ProfileService profileService;


    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO) {
        try{
            if(!profileService.isProfileActive(authDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message","Account is not active.Please activate your account first"
                ));
            }
            Map<String,Object> response= profileService.authenticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage()
            ));
        }

    }



}
