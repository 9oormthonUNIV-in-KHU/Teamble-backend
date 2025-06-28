package backend.teamble.message;

import backend.teamble.message.Message;
import backend.teamble.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByProjectId(Long projectId);
    List<Message> findByProjectIdAndTypeIn(Long projectId, List<String> types);
    List<Message> findByProject(Project project);
}