package backend.teamble.message;

import backend.teamble.message.dto.ChatMessageRequest;
import backend.teamble.project.Project;
import backend.teamble.project.ProjectRepository;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MessageIntegrationTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReadReceiptRepository readReceiptRepository;

    @Autowired
    private MessageRepository messageRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User sender;
    private User receiver1;
    private User receiver2;
    private Project project;

    @BeforeEach
    void setup() {
        readReceiptRepository.deleteAll();
        messageRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();

        project = new Project();
        project.setName("테스트 프로젝트");
        project.setTopic("주제");
        projectRepository.save(project);

        sender = new User();
        sender.setName("보낸이");
        sender.setEmail("sender@example.com");
        sender.setPassword("password");
        sender.setProject(project);
        userRepository.save(sender);

        receiver1 = new User();
        receiver1.setName("수신자1");
        receiver1.setEmail("recv1@example.com");
        receiver1.setPassword("password");
        receiver1.setProject(project);
        userRepository.save(receiver1);

        receiver2 = new User();
        receiver2.setName("수신자2");
        receiver2.setEmail("recv2@example.com");
        receiver2.setPassword("password");
        receiver2.setProject(project);
        userRepository.save(receiver2);
    }

    @Test
    @DisplayName("일반 메시지 전송 테스트")
    void sendGeneralMessageTest() throws Exception {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setUserId(sender.getId());
        request.setProjectId(project.getId());
        request.setContent("안녕하세요 일반 메시지입니다.");
        request.setType("text");

        System.out.println("\n[REQUEST] 일반 메시지 전송");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));

        messageService.sendMessage(request);

        System.out.println("[RESPONSE] 일반 메시지 전송 성공");
    }


    @Test
    @DisplayName("읽음 처리 테스트")
    void markAsReadTest() throws Exception {
        ChatMessageRequest request = new ChatMessageRequest();
        request.setUserId(sender.getId());
        request.setProjectId(project.getId());
        request.setContent("읽음 테스트 메시지");
        request.setType("text");
        request.setTargetUserIds(List.of(receiver1.getId()));

        messageService.sendMessage(request);

        Message msg = messageRepository.findAll().get(0);

        System.out.println("\n[REQUEST] 읽음 처리 요청");
        System.out.println("{ \"userId\": " + receiver1.getId() + ", \"messageId\": " + msg.getId() + " }");

        messageService.markAsRead(receiver1, msg.getId());

        boolean isRead = readReceiptRepository.findByMessageIdAndUserId(msg.getId(), receiver1.getId())
                .map(ReadReceipt::getIsRead)
                .orElse(false);

        System.out.println("[RESPONSE] 읽음 여부: " + isRead);
    }


    @Test
    @DisplayName("메시지 여러 개 전송 후 확인")
    void multipleMessageSendAndCheck() throws Exception {
        for (int i = 1; i <= 3; i++) {
            ChatMessageRequest request = new ChatMessageRequest();
            request.setUserId(sender.getId());
            request.setProjectId(project.getId());
            request.setContent("메시지 " + i);
            request.setType("text");
            messageService.sendMessage(request);
        }

        List<Message> allMessages = messageRepository.findAll();
        System.out.println("총 메시지 수: " + allMessages.size());
        for (Message message : allMessages) {
            System.out.println("ID: " + message.getId());
            System.out.println("Content (Encrypted): " + message.getContent());
            System.out.println("User: " + message.getUser().getEmail());
            System.out.println("Timestamp: " + message.getTimestamp());
            System.out.println("--------------------");
        }

        assert allMessages.size() == 3;
    }
}