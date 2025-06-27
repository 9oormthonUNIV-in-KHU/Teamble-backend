package backend.teamble.message;

import backend.teamble.message.dto.ChatMessageRequest;
import backend.teamble.message.dto.ChatMessageResponse;
import backend.teamble.project.Project;
import backend.teamble.project.ProjectRepository;
import backend.teamble.security.AESUtil;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ReadReceiptRepository readReceiptRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(ChatMessageRequest request) throws Exception {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Project project = projectRepository.findById(request.getProjectId()).orElseThrow();

        String encryptedContent = AESUtil.encrypt(request.getContent());

        Message message = new Message();
        message.setUser(user);
        message.setProject(project);
        message.setContent(encryptedContent);
        message.setType(request.getType());
        message.setDeleted(false);

        Message saved = messageRepository.save(message);

        // 읽음 처리
        List<ReadReceipt> receipts = new ArrayList<>();
        if (request.getTargetUserIds() != null) {
            for (Long uid : request.getTargetUserIds()) {
                User target = userRepository.findById(uid).orElseThrow();
                receipts.add(new ReadReceipt(null, saved, target, false));
            }
        }
        readReceiptRepository.saveAll(receipts);

        messagingTemplate.convertAndSend(
                "/sub/project/" + request.getProjectId(),
                new ChatMessageResponse(
                        saved.getId(),
                        saved.getUser().getId(),
                        request.getContent(),
                        request.getType(),
                        saved.getTimestamp()
                )
        );
    }

    public void markAsRead(User user, Long messageId) {
        ReadReceipt receipt = readReceiptRepository.findByMessageIdAndUserId(messageId, user.getId())
                .orElseThrow(() -> new RuntimeException("읽음 정보 없음"));
        receipt.setIsRead(true);
        readReceiptRepository.save(receipt);
    }

}