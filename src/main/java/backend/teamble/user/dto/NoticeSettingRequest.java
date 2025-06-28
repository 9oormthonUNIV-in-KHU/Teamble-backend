package backend.teamble.user.dto;

import lombok.Data;

@Data
public class NoticeSettingRequest {
    private Boolean chatNotice;
    private Boolean scheduleNotice;
}
