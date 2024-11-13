package com.example.ex32_jpa_qnaboard_swagger.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ex32_jpa_qnaboard_swagger.security.TokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value.");
            }

            // UserEntity user = new UserEntity();
            // user.setUsername(userDTO.getUsername());
            // user.setUsername(userDTO.getPassword());
            // user.setUsername(userDTO.getEmail());

            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .email(userDTO.getEmail()).build();

            UserEntity registeredUser = userService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .email(registeredUser.getEmail())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User creation failed");
        }
    }

    @PostMapping("/signin")
    @Operation(summary = "사용자 로그인", description = "이메일과 비밀번호로 로그인하여 인증 토큰을 받습니다.")
    public ResponseEntity<?> authenticate(
        @Parameter(description = "사용자의 이메일 주소", schema = @Schema(example = "{\"password\": \"1234\",\"email\": \"kim1@example.com\"}"))
        @RequestBody UserDTO userDTO
    ) {
        try {
            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

            if (user != null) {
                final String token = tokenProvider.create(user);

                final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .email(user.getEmail())
                    .build();
                return ResponseEntity.ok().body(responseUserDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid email or password");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while preocessing the request");
        }
    }
}
