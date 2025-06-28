package backend.teamble.project;

import backend.teamble.project.dto.MyTeamsResponse;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    @Transactional
    public Project createTeam(String teamName, String topic, User user) {
        Project project = new Project(teamName, topic, user);
        projectRepository.save(project);

        Membership membership = new Membership(user, project);
        membershipRepository.save(membership);

        return project;
    }

    public List<MyTeamsResponse> getMyTeams(User user) {
        List<Membership> memberships = membershipRepository.findByUser(user);

        return memberships.stream()
                .map(membership -> {
                    Project project = membership.getProject();
                    return new MyTeamsResponse(project.getId(), project.getName());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void inviteUser(Long teamId, String email, User inviter) {
        Project team = projectRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀이 존재하지 않습니다."));

        if (!membershipRepository.existsByProjectAndUser(team, inviter)) {
            throw new IllegalStateException("초대자는 이 팀의 구성원이 아닙니다.");
        }

        User invitee = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

        if (membershipRepository.existsByProjectAndUser(team, invitee)) {
            throw new IllegalStateException("이미 팀에 참여 중인 사용자입니다.");
        }

        Membership membership = new Membership(invitee, team);
        membershipRepository.save(membership);
    }

}
