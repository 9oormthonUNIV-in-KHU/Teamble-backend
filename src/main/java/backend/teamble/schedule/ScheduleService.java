package backend.teamble.schedule;

import backend.teamble.project.MembershipRepository;
import backend.teamble.project.Project;
import backend.teamble.project.ProjectRepository;
import backend.teamble.schedule.dto.CreateScheduleRequest;
import backend.teamble.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;

    @Transactional
    public Schedule createSchedule(CreateScheduleRequest request, User user) {
        Project team = projectRepository.findById(request.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("해당 팀이 존재하지 않습니다."));

        boolean isMember = membershipRepository.existsByProjectAndUser(team, user);
        if (!isMember) {
            throw new IllegalStateException("해당 팀의 구성원이 아닙니다.");
        }

        Schedule schedule = new Schedule(request.getTitle(), request.getDate(), team, user);
        return scheduleRepository.save(schedule);
    }
}
