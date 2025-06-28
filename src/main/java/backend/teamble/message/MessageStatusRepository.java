package backend.teamble.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageStatusRepository extends JpaRepository<MessageStatus, Long> {
    Optional<MessageStatus> findByMessageIdAndUserId(Long messageId, Long userId);
    List<MessageStatus> findAllByMessageIdAndIsReadTrue(Long messageId);
    List<MessageStatus> findByUserId(Long userId);
    List<MessageStatus> findByMessageId(Long messageId);
}