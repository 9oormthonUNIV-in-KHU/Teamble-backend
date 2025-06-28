package backend.teamble.user;

import backend.teamble.user.dto.UserLoginRequest;
import backend.teamble.user.dto.UserSignupRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String testEmail = "login@example.com";
    private final String testPassword = "securepass";

    @BeforeEach
    void cleanTestUser() {
        userRepository.deleteByEmail(testEmail);
    }

    @Test
    @DisplayName("회원가입 → 로그인 → 로그아웃 성공 흐름")
    void userSignupLoginLogout() throws Exception {
        // ------------------------------
        // 1. 회원가입 요청
        // ------------------------------
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setName("로그인유저");
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);

        String signupJson = objectMapper.writeValueAsString(signupRequest);
        System.out.println("▶ 회원가입 요청:\n" + signupJson);

        MvcResult signupResult = mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
                .andReturn();

        String signupResponse = signupResult.getResponse().getContentAsString();
        System.out.println("◀ 회원가입 응답:\n" + signupResponse);

        // ------------------------------
        // 2. 로그인 요청
        // ------------------------------
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);

        String loginJson = objectMapper.writeValueAsString(loginRequest);
        System.out.println("▶ 로그인 요청:\n" + loginJson);

        MvcResult loginResult = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        System.out.println("◀ 로그인 응답:\n" + loginResponse);

        JsonNode loginNode = objectMapper.readTree(loginResponse);
        String token = loginNode.get("token").asText();

        // ------------------------------
        // 3. 로그아웃 요청
        // ------------------------------
        System.out.println("▶ 로그아웃 요청: Authorization Bearer 토큰 포함");

        MvcResult logoutResult = mockMvc.perform(post("/users/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String logoutResponse = logoutResult.getResponse().getContentAsString();
        System.out.println("◀ 로그아웃 응답:\n" + logoutResponse);
    }
}