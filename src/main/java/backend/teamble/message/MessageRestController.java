package backend.teamble.message;

import backend.teamble.message.dto.ChatMessageRequest;
import backend.teamble.message.dto.ChatMessageResponse;
import backend.teamble.security.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageService messageService;
    private final MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<?> sendGeneralMessage(@RequestBody ChatMessageRequest request) throws Exception {
        request.setType("text");
        ChatMessageResponse response = messageService.sendMessage(request);
        return ResponseEntity.ok(
                Map.of(
                        "messageId", response.getMessageId(),
                        "timestamp", response.getTimestamp()
                )
        );
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendRequestMessage(@RequestBody ChatMessageRequest request) {
        Long messageId = messageService.sendRequestMessage(request);
        return ResponseEntity.ok(Map.of(
                "messageId", messageId,
                "message", "요청 메시지가 전송되었습니다."
        ));
    }

    @PostMapping("/request/{messageId}/respond")
    public ResponseEntity<?> respondToRequest(
            @PathVariable Long messageId,
            @RequestBody Map<String, Object> body) {

        Long userId = Long.valueOf(body.get("userId").toString());
        String response = body.get("response").toString();

        messageService.respondToRequest(messageId, userId, response);
        return ResponseEntity.ok(Map.of("message", "요청에 응답하였습니다."));
    }

    @GetMapping("/request")
    public ResponseEntity<?> getRequestMessages(@RequestParam("roomId") Long roomId) throws Exception {
        List<String> requestTypes = List.of("document_request", "confirmation_request");
        List<Message> messages = messageRepository.findByProjectIdAndTypeIn(roomId, requestTypes);

        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Message message : messages) {
            List<Map<String, Object>> targets = new ArrayList<>();

            for (MessageStatus status : message.getMessageStatuses()) {
                Map<String, Object> targetMap = new HashMap<>();
                targetMap.put("userId", status.getUser().getId());
                targetMap.put("name", status.getUser().getName());
                targetMap.put("responded", status.getResponded());
                targets.add(targetMap);
            }

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("messageId", message.getId());
            messageMap.put("type", message.getType());
            messageMap.put("content", AESUtil.decrypt(message.getContent()));
            messageMap.put("targetUsers", targets);

            responseList.add(messageMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("requests", responseList);

        return ResponseEntity.ok(result);
    }
}