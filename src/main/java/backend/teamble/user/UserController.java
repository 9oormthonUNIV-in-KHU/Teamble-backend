package backend.teamble.user;

import backend.teamble.user.dto.JwtTokenResponse;
import backend.teamble.user.dto.UserLoginRequest;
import backend.teamble.user.dto.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        Long userId = userService.signup(request);
        return ResponseEntity.ok().body(
                new SignupResponse(userId, "회원가입이 완료되었습니다.")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        JwtTokenResponse jwtTokenResponse = userService.login(request);
        return ResponseEntity.ok().body(jwtTokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok().body(new MessageResponse("로그아웃 성공"));
    }

    private record SignupResponse(Long userId, String message) {}
    private record MessageResponse(String message) {}
}
