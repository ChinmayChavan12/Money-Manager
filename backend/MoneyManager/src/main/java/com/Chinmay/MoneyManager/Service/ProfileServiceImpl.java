package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.AuthRequest;
import com.Chinmay.MoneyManager.IO.AuthResponse;
import com.Chinmay.MoneyManager.IO.ProfileRequest;
import com.Chinmay.MoneyManager.IO.ProfileResponse;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.ProfileEntityRepository;
import com.Chinmay.MoneyManager.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public Boolean isProfileActive(String email) {
        return profileEntityRepository.findByActivationToken(email).map(ProfileEntity::getIsActive).orElse(false);
    }

    @Override
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileEntityRepository.findByEmail(authentication.getName()).orElseThrow(()->new RuntimeException("Profile Not Found with this email: "+authentication.getName()));
    }

    @Override
    public ProfileResponse getPublicProfile(String email) {
        ProfileEntity currentUser=null;
        if(email==null){
          currentUser= getCurrentProfile();
        }else{
            currentUser=profileEntityRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Profile Not Found with this email: "+email));

        }
        return ProfileResponse.builder().name(currentUser.getName()).email(currentUser.getEmail()).isActive(currentUser.getIsActive()).build();
    }

    @Override
    public Optional<ProfileEntity> getProfile(String email) {
        return profileEntityRepository.findByEmail(email);
    }

    @Override
    public AuthResponse authenticateAndGenerateToken(AuthRequest authRequest) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            final UserDetails userDetails= appUserDetailsService.loadUserByUsername(authRequest.getEmail());
            final String jwtToken= jwtUtil.generateToken(userDetails);
            return new AuthResponse(jwtToken,userDetails.getUsername());
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid Credentials");
        }

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
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .profileImageUrl(profileRequest.getProfileImageUrl())
                .build();
    }

}
