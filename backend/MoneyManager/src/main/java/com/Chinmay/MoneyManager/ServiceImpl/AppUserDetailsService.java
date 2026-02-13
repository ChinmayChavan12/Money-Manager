package com.Chinmay.MoneyManager.ServiceImpl;

import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.ProfileEntityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileEntityRepository profileEntityRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity existingPrfoile=profileEntityRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email));
        return User.builder()
                .username(existingPrfoile.getEmail())
                .password(existingPrfoile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
