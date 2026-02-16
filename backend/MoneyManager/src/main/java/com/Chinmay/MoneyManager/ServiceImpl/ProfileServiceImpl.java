package com.Chinmay.MoneyManager.ServiceImpl;

import com.Chinmay.MoneyManager.IO.*;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.ProfileEntityRepository;
import com.Chinmay.MoneyManager.Service.EmailService;
import com.Chinmay.MoneyManager.Service.ProfileService;
import com.Chinmay.MoneyManager.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileEntityRepository profileEntityRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity newProfileEntity = toEntity(profileDTO);
        newProfileEntity.setActivationToken(UUID.randomUUID().toString());
        newProfileEntity = profileEntityRepository.save(newProfileEntity);
        String activationLink="http://localhost:8080/activate?token="+newProfileEntity.getActivationToken();
        String subject="Account Activation Email";
        String text="Click on the following link to activate your account: "+activationLink;
        emailService.sendEmail(newProfileEntity.getEmail(),subject,text);
        return toDTO(newProfileEntity);

    }

    @Override
    public boolean activateProfile(String activationToken) {
        return  profileEntityRepository.findByActivationToken(activationToken).map(profileEntity ->{
            profileEntity.setIsActive(true);
            profileEntityRepository.save(profileEntity);
            return true;
        }).orElse(false);
    }

    @Override
    public Boolean isProfileActive(String email) {
        return profileEntityRepository.findByEmail(email) // query by email
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    @Override
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileEntityRepository.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("Profile Not Found with this email: "+authentication.getName()));
    }

    @Override
    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currentUser=null;
        if(email==null){
          currentUser= getCurrentProfile();
        }else{
            currentUser=profileEntityRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Profile Not Found with this email: "+email));

        }
        return ProfileDTO.builder().id(currentUser.getId()).name(currentUser.getName()).email(currentUser.getEmail()).profileImageUrl(currentUser.getProfileImageUrl()).createdAt(currentUser.getCreatedAt()).updatedAt(currentUser.getUpdatedAt()).build();
    }

    @Override
    public Optional<ProfileEntity> getProfile(String email) {
        return profileEntityRepository.findByEmail(email);
    }

    @Override
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            //Generate JWT token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authDTO.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    private ProfileDTO toDTO(ProfileEntity newProfileEntity) {
        return ProfileDTO.builder()
                .id(newProfileEntity.getId())
                .name(newProfileEntity.getName())
                .email(newProfileEntity.getEmail())
                .profileImageUrl(newProfileEntity.getProfileImageUrl())
                .createdAt(newProfileEntity.getCreatedAt())
                .updatedAt(newProfileEntity.getUpdatedAt())
                .build();
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO){
        return ProfileEntity.builder().id(profileDTO.getId())
                .name(profileDTO.getName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

}
