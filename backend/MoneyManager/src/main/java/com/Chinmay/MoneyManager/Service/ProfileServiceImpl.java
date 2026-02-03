package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.ProfileRequest;
import com.Chinmay.MoneyManager.IO.ProfileResponse;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.ProfileEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileEntityRepository profileEntityRepository;
    private final EmailService emailService;

    public ProfileResponse registerProfile(ProfileRequest profileRequest) {
        ProfileEntity newProfileEntity = toEntity(profileRequest);
        newProfileEntity.setActivationToken(UUID.randomUUID().toString());
        newProfileEntity = profileEntityRepository.save(newProfileEntity);
        String activationLink="http://localhost:8080/activate?token="+newProfileEntity.getActivationToken();
        String subject="Account Activation Email";
        String text="Click on the following link to activate your account: "+activationLink;
        emailService.sendActivationEmail(newProfileEntity.getEmail(),subject,text);
        return toResponse(newProfileEntity);

    }

    @Override
    public boolean activateProfile(String activationToken) {
        return  profileEntityRepository.findByActivationToken(activationToken).map(profileEntity ->{
            profileEntity.setIsActive(true);
            profileEntity.setActivationToken(null);
            profileEntityRepository.save(profileEntity);
            return true;
        }).orElse(false);
    }

    private ProfileResponse toResponse(ProfileEntity newProfileEntity) {
        return ProfileResponse.builder()
                .name(newProfileEntity.getName())
                .email(newProfileEntity.getEmail())
                .isActive(newProfileEntity.getIsActive())
                .build();
    }

    public ProfileEntity toEntity(ProfileRequest profileRequest){
        return ProfileEntity.builder()
                .name(profileRequest.getName())
                .email(profileRequest.getEmail())
                .password(profileRequest.getPassword())
                .profileImageUrl(profileRequest.getProfileImageUrl())
                .build();
    }
}
