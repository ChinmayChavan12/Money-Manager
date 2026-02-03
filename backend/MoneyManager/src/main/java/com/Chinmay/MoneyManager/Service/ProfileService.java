package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.ProfileRequest;
import com.Chinmay.MoneyManager.IO.ProfileResponse;

public interface ProfileService {
    ProfileResponse registerProfile(ProfileRequest profileRequest);
    boolean activateProfile(String activationToken);
}
