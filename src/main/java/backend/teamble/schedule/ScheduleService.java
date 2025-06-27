package backend.teamble.schedule;

import backend.teamble.project.Membership;
import backend.teamble.project.MembershipRepository;
import backend.teamble.project.Project;
import backend.teamble.project.ProjectRepository;
import backend.teamble.schedule.dto.CreateScheduleRequest;
import backend.teamble.schedule.dto.MyScheduleResponse;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

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

    public List<MyScheduleResponse> getMySchedules(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 유저가 속한 모든 프로젝트(team)
        List<Project> projects = user.getMemberships()
                .stream()
                .map(Membership::getProject)
                .toList();

        List<MyScheduleResponse> responses = new ArrayList<>();

        for (Project project : projects) {
            scheduleRepository.findByProject(project).forEach(schedule ->
                    responses.add(new MyScheduleResponse(schedule.getId(), schedule.getTitle(), schedule.getEndTime()))
            );
        }

        return responses;
    }
}
