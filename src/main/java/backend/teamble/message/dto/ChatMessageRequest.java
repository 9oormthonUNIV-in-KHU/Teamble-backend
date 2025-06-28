package backend.teamble.message.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatMessageRequest {
    private Long roomId;                 // 변경됨: 기존 projectId
    private Long senderId;              // 변경됨: 기존 userId
    private String content;
    private String type;                // "text", "document_request", "confirmation_request"
    private List<Long> targetUserIds;   // 요청 대상
    private LocalDateTime expiresAt;    // 요청 메시지 만료 시간
}