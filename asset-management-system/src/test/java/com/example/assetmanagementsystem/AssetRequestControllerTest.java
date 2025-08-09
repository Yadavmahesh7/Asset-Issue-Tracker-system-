package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.dto.AssetRequestDto;
import com.example.assetmanagementsystem.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = MailSenderAutoConfiguration.class)
public class AssetRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssetRequestRepository assetRequestRepository;

    @MockBean
    private AccessoryRepository accessoryRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private com.example.assetmanagementsystem.service.NotificationService notificationService;

    @Test
    @WithMockUser(username = "testuser")
    public void testCreateRequest() throws Exception {
        AssetRequestDto assetRequestDto = new AssetRequestDto();
        assetRequestDto.setAccessoryId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Accessory accessory = new Accessory();
        accessory.setId(1L);
        accessory.setName("Laptop");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(assetRequestRepository.save(any())).thenReturn(new AssetRequest());

        mockMvc.perform(post("/api/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetRequestDto)))
                .andExpect(status().isOk());
    }
}
