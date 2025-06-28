package backend.teamble.message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadReceiptRepository extends JpaRepository<ReadReceipt, Long> {
    List<ReadReceipt> findByUserId(Long userId);
    List<ReadReceipt> findByMessageId(Long messageId);
    Optional<ReadReceipt> findByMessageIdAndUserId(Long messageId, Long userId);
}