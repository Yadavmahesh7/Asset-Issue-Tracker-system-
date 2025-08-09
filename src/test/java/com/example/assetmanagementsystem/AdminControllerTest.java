package com.example.assetmanagementsystem;

import com.example.assetmanagementsystem.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = MailSenderAutoConfiguration.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetRequestRepository assetRequestRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuditLogService auditLogService;

    @MockBean
    private com.example.assetmanagementsystem.service.NotificationService notificationService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testApproveRequest() throws Exception {
        User requestUser = new User();
        requestUser.setId(1L);
        requestUser.setUsername("testuser");
        requestUser.setEmail("test@test.com");

        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");

        Accessory accessory = new Accessory();
        accessory.setId(1L);
        accessory.setName("Laptop");

        AssetRequest request = new AssetRequest();
        request.setId(1L);
        request.setUser(requestUser);
        request.setAccessory(accessory);

        when(assetRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(assetRequestRepository.save(any(AssetRequest.class))).thenReturn(request);
        doNothing().when(auditLogService).log(any(User.class), anyString(), anyString());
        doNothing().when(notificationService).sendNotification(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/admin/requests/1/approve"))
                .andExpect(status().isOk());
    }
}
