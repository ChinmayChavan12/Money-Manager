package com.Chinmay.MoneyManager.Controller;

import com.Chinmay.MoneyManager.IO.AuthRequest;
import com.Chinmay.MoneyManager.IO.AuthResponse;
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

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ProfileService profileService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        ProfileEntity existingUser = profileService
                .getProfile(authRequest.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + authRequest.getEmail()
                        )
                );

        if (!existingUser.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            "Account is not active. Please activate your account first."
                    ));
        }

        try {
            AuthResponse response =
                    profileService.authenticateAndGenerateToken(authRequest);
            return ResponseEntity.ok(response);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    }



}
