package backend.teamble.user;

import backend.teamble.security.CustomUserDetails;
import backend.teamble.user.dto.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

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

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }

    @PatchMapping("/me/settings")
    public ResponseEntity<NoticeSettingResponse> updateSettings(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody NoticeSettingRequest request) {

        userService.updateNoticeSettings(userDetails.getUser(), request);
        return ResponseEntity.ok(new NoticeSettingResponse("설정이 업데이트되었습니다."));
    }

    private record SignupResponse(Long userId, String message) {}
    private record MessageResponse(String message) {}
}
