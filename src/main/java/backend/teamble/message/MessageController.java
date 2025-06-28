package backend.teamble.message;

import backend.teamble.message.dto.ChatMessageRequest;
import backend.teamble.message.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @MessageMapping("/messages")
    public void sendGeneralMessage(ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        request.setType("text");
        messageService.sendMessage(request);
    }

    @MessageMapping("/messages/request")
    public void sendRequestMessage(ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        // type은 프론트에서 document_request 또는 response_request 등으로 구분해줄 것
        messageService.sendMessage(request);
    }
}