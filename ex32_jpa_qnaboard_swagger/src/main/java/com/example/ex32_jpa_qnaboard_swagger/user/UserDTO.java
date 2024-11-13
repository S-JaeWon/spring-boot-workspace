package com.example.ex32_jpa_qnaboard_swagger.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String token;

    private Long id;

    private String username;

    private String password;

    private String email;
}