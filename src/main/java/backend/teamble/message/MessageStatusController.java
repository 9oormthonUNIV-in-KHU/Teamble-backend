package backend.teamble.message;

import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message-status")
@RequiredArgsConstructor
public class MessageStatusController {

    private final MessageStatusRepository messageStatusRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;

    @PostMapping("/read")
    public ResponseEntity<?> markAsRead(@RequestParam Long userId, @RequestParam Long messageId) {
        User user = userRepository.findById(userId).orElseThrow();
        messageService.markAsRead(user, messageId);
        return ResponseEntity.ok("읽음 처리 완료");
    }

    @PostMapping("/read/all")
    public ResponseEntity<?> markAllAsRead(@RequestParam Long userId, @RequestParam Long projectId) {
        messageService.markAllAsRead(userId, projectId);
        return ResponseEntity.ok("해당 프로젝트의 모든 메시지 읽음 처리 완료");
    }

    @PostMapping("/respond")
    public ResponseEntity<?> markAsResponded(
            @RequestParam Long userId,
            @RequestParam Long messageId,
            @RequestParam String response) {

        User user = userRepository.findById(userId).orElseThrow();
        messageService.markAsResponded(user, messageId, response);
        return ResponseEntity.ok("응답 처리 완료");
    }
}