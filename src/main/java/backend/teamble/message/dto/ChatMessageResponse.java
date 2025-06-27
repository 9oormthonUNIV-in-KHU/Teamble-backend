package backend.teamble.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long userId;
    private String content;
    private String type;
    private LocalDateTime timestamp;
}