package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.*;
import com.Chinmay.MoneyManager.Model.ProfileEntity;

import java.util.Map;
import java.util.Optional;

public interface ProfileService {
    ProfileDTO registerProfile(ProfileDTO profileDTO);
    boolean activateProfile(String activationToken);
    Boolean isProfileActive(String email);
    ProfileEntity getCurrentProfile();
    ProfileDTO getPublicProfile(String email);
    Optional<ProfileEntity> getProfile(String email);

    Map<String,Object> authenticateAndGenerateToken(AuthDTO authDTO);
}
