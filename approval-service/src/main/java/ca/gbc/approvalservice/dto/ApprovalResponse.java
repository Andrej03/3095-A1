package ca.gbc.approvalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponse {
    private String id;
    private Long eventId;
    private Long userId;
    private boolean approved;
    private String approvalStatus;
}
