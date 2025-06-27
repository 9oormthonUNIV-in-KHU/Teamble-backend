package backend.teamble.schedule;

import backend.teamble.schedule.dto.CreateScheduleRequest;
import backend.teamble.schedule.dto.CreateScheduleResponse;
import backend.teamble.security.CustomUserDetails;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import backend.teamble.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        Schedule schedule = scheduleService.createSchedule(request, user);
        return ResponseEntity.ok(new CreateScheduleResponse(schedule.getId(), "일정이 등록되었습니다."));
    }

}
