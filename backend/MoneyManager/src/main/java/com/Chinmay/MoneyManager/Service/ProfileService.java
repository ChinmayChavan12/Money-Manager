package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.AuthRequest;
import com.Chinmay.MoneyManager.IO.AuthResponse;
import com.Chinmay.MoneyManager.IO.ProfileRequest;
import com.Chinmay.MoneyManager.IO.ProfileResponse;
import com.Chinmay.MoneyManager.Model.ProfileEntity;

import java.util.Optional;

public interface ProfileService {
    ProfileResponse registerProfile(ProfileRequest profileRequest);
    boolean activateProfile(String activationToken);
    Boolean isProfileActive(String email);
    ProfileEntity getCurrentProfile();
    ProfileResponse getPublicProfile(String email);
    Optional<ProfileEntity> getProfile(String email);

    AuthResponse authenticateAndGenerateToken(AuthRequest authRequest);
}
