package backend.teamble.user;

import backend.teamble.user.dto.UserLoginRequest;
import backend.teamble.user.dto.UserSignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void threeUsersFlow() throws Exception {
        String[] emails = {
                "testuser1@example.com",
                "testuser2@example.com",
                "testuser3@example.com"
        };
        String[] names = {"유저1", "유저2", "유저3"};
        String[] passwords = {"pw1!Strong", "pw2!Strong", "pw3!Strong"};

        for (int i = 0; i < 3; i++) {
            // 회원가입
            UserSignupRequest signupRequest = new UserSignupRequest();
            signupRequest.setName(names[i]);
            signupRequest.setEmail(emails[i]);
            signupRequest.setPassword(passwords[i]);
            String signupJson = objectMapper.writeValueAsString(signupRequest);

            MvcResult signupResult = mockMvc.perform(post("/users/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signupJson))
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println("📦 [회원가입 요청 - " + names[i] + "]: " + signupJson);
            System.out.println("📬 [회원가입 응답 - " + names[i] + "]: " + signupResult.getResponse().getContentAsString());

            // 로그인
            UserLoginRequest loginRequest = new UserLoginRequest();
            loginRequest.setEmail(emails[i]);
            loginRequest.setPassword(passwords[i]);
            String loginJson = objectMapper.writeValueAsString(loginRequest);

            MvcResult loginResult = mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(loginJson))
                    .andExpect(status().isOk())
                    .andReturn();

            String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

            System.out.println("📦 [로그인 요청 - " + names[i] + "]: " + loginJson);
            System.out.println("📬 [로그인 응답 - " + names[i] + "]: " + loginResult.getResponse().getContentAsString());

            // 로그아웃
            MvcResult logoutResult = mockMvc.perform(post("/users/logout")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println("📦 [로그아웃 요청 - " + names[i] + "]");
            System.out.println("📬 [로그아웃 응답 - " + names[i] + "]: " + logoutResult.getResponse().getContentAsString());
            System.out.println("---------------------------------------------------------");
        }
    }
}
