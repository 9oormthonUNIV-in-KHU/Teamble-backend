package backend.teamble.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestMessageResponse {
    private Long messageId;
    private String type;
    private String content;
    private List<TargetUserResponse> targetUsers;

    @Data
    @AllArgsConstructor
    public static class TargetUserResponse {
        private Long userId;
        private String name;
        private String responded; // ← boolean이 아니라 String이어야 함 ("pending", "accepted", "rejected")
    }
}