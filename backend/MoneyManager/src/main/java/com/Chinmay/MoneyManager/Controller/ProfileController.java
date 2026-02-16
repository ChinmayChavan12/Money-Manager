package com.Chinmay.MoneyManager.Controller;

import com.Chinmay.MoneyManager.IO.ProfileDTO;
import com.Chinmay.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> register(@RequestBody ProfileDTO profileDTO) {

        ProfileDTO response=  profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {
        boolean isActivated = profileService.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated Successfully");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Activation Token not found or already used");
        }
    }
    @GetMapping("/test")
    public String greet(){
        return "Hello World";
    }
}

