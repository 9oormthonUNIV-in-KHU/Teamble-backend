package backend.teamble.project;

import backend.teamble.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findByUser(User user);

    boolean existsByProjectAndUser(Project team, User inviter);

    List<Membership> findByProjectId(Long teamId);
}
