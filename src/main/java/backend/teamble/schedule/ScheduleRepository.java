package backend.teamble.schedule;

import backend.teamble.project.Membership;
import backend.teamble.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByProject(Project project);
}
