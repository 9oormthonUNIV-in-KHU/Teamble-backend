package backend.teamble.message.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChatMessageRequest {
    private Long projectId;
    private Long userId;
    private String type; // "text", "document_request", "response_request"
    private String content;
    private List<Long> targetUserIds; // 요청 메시지 시 대상 유저 ID
}