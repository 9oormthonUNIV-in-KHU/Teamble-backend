package backend.teamble.user;

import backend.teamble.config.jwt.JwtTokenProvider;
import backend.teamble.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Long signup(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        User user = new User();
        user.setId(null); // auto-generated
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setChatNotice(true);
        user.setScheduleNotice(true);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public JwtTokenResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtTokenProvider.createToken(user.getEmail());
        return new JwtTokenResponse(token, user.getId());
    }
    public void logout(String token) {
        // 실제로는 클라이언트가 토큰을 삭제하면 됨.
        // 서버 측에서는 "정상 로그아웃 처리됨" 정도만 응답
        System.out.println("로그아웃 요청 받은 토큰: " + token);
    }

    public void updateNoticeSettings(User user, NoticeSettingRequest request) {
        if (request.getChatNotice() != null) {
            user.setChatNotice(request.getChatNotice());
        }
        if (request.getScheduleNotice() != null) {
            user.setScheduleNotice(request.getScheduleNotice());
        }
        userRepository.save(user);
    }
}
