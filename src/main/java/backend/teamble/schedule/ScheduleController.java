package backend.teamble.schedule;

import backend.teamble.schedule.dto.CreateScheduleRequest;
import backend.teamble.schedule.dto.CreateScheduleResponse;
import backend.teamble.schedule.dto.MyScheduleResponse;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public Map<String, List<MyScheduleResponse>> getMySchedules(@RequestParam String userId, Principal principal) {
        // userId가 "me"인 경우에만 현재 로그인 유저의 일정 조회
        if (!"me".equals(userId)) {
            throw new IllegalArgumentException("현재 로그인한 유저만 일정을 조회할 수 있습니다.");
        }

        List<MyScheduleResponse> schedules = scheduleService.getMySchedules(principal.getName());
        return Map.of("schedules", schedules);
    }

}
