package com.Chinmay.MoneyManager.IO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    private String name;
    private String email;
    private String password;
    private String profileImageUrl;

}
