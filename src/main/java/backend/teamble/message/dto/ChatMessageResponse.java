package backend.teamble.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private LocalDateTime timestamp;
}