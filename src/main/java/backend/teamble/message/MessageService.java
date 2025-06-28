package backend.teamble.message;

import backend.teamble.message.dto.ChatMessageRequest;
import backend.teamble.message.dto.ChatMessageResponse;
import backend.teamble.project.Project;
import backend.teamble.project.ProjectRepository;
import backend.teamble.security.AESUtil;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageResponse sendMessage(ChatMessageRequest request) throws Exception {
        log.info("📨 sendMessage 호출 - senderId: {}, roomId: {}, content: {}", request.getSenderId(), request.getRoomId(), request.getContent());

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Project project = projectRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Project (Room) not found"));

        String encryptedContent = AESUtil.encrypt(request.getContent());

        Message message = new Message();
        message.setUser(sender);
        message.setProject(project);
        message.setContent(encryptedContent);
        message.setType(request.getType());
        message.setDeleted(false);
        message.setExpiresAt(request.getExpiresAt());

        Message saved = messageRepository.save(message);

        // 요청 메시지인 경우 대상자에 대해 상태 저장
        if (request.getTargetUserIds() != null && !request.getTargetUserIds().isEmpty()) {
            List<MessageStatus> statuses = new ArrayList<>();
            for (Long uid : request.getTargetUserIds()) {
                User target = userRepository.findById(uid).orElseThrow();
                statuses.add(new MessageStatus(null, saved, target, false, "pending"));
            }
            messageStatusRepository.saveAll(statuses);
        }

        messagingTemplate.convertAndSend(
                "/sub/project/" + request.getRoomId(),
                new ChatMessageResponse(saved.getId(), saved.getTimestamp())
        );

        return new ChatMessageResponse(saved.getId(), saved.getTimestamp());
    }

    public Long sendRequestMessage(ChatMessageRequest request) {
        try {
            ChatMessageResponse response = sendMessage(request);
            return response.getMessageId();
        } catch (Exception e) {
            throw new RuntimeException("요청 메시지 전송 실패", e);
        }
    }

    public void markAsRead(User user, Long messageId) {
        MessageStatus status = messageStatusRepository.findByMessageIdAndUserId(messageId, user.getId())
                .orElseThrow(() -> new RuntimeException("MessageStatus not found"));
        status.setIsRead(true);
        messageStatusRepository.save(status);
    }

    public void markAllAsRead(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();

        List<Message> messages = messageRepository.findByProject(project);
        for (Message message : messages) {
            Optional<MessageStatus> optional = messageStatusRepository.findByMessageIdAndUserId(message.getId(), userId);
            if (optional.isPresent()) {
                MessageStatus status = optional.get();
                if (!status.getIsRead()) {
                    status.setIsRead(true);
                    messageStatusRepository.save(status);
                }
            } else {
                MessageStatus newStatus = new MessageStatus(null, message, user, true, "pending");
                messageStatusRepository.save(newStatus);
            }
        }
    }

    public void markAsResponded(User user, Long messageId, String response) {
        if (!response.equals("accepted") && !response.equals("rejected")) {
            throw new IllegalArgumentException("응답은 'accepted' 또는 'rejected'만 가능합니다.");
        }
        MessageStatus status = messageStatusRepository.findByMessageIdAndUserId(messageId, user.getId())
                .orElseThrow(() -> new RuntimeException("MessageStatus not found"));

        status.setResponded(response);
        messageStatusRepository.save(status);
        log.info("✅ 응답 완료 - user {} → message {} : {}", user.getId(), messageId, response);
    }

    public void respondToRequest(Long messageId, Long userId, String response) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        markAsResponded(user, messageId, response);
    }
}