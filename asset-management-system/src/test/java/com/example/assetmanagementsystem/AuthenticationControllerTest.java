package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.dto.AuthRequest;
import com.example.assetmanagementsystem.dto.UserDto;
import com.example.assetmanagementsystem.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = MailSenderAutoConfiguration.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JpaUserDetailsService userDetailsService;

    @MockBean
    private com.example.assetmanagementsystem.service.NotificationService notificationService;

    @Test
    public void testRegister() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@test.com");
        userDto.setRoles(Collections.singleton(Role.EMPLOYEE));

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");

        when(passwordEncoder.encode(any())).thenReturn("encodedpassword");
        when(userRepository.save(any())).thenReturn(savedUser);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticate() throws Exception {
        AuthRequest authRequest = new AuthRequest("testuser", "password");

        when(userDetailsService.loadUserByUsername(any())).thenReturn(
                new org.springframework.security.core.userdetails.User("testuser", "password", Collections.emptyList())
        );
        when(jwtUtil.generateToken(any())).thenReturn("test-jwt");

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());
    }
}
