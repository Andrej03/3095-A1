package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApprovalResponse> createApproval(@RequestBody ApprovalRequest approvalRequest) {
        ApprovalResponse approvalResponse = approvalService.createApproval(approvalRequest);
        return new ResponseEntity<>(approvalResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApprovalResponse> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<ApprovalResponse> getApprovalById(@PathVariable("approvalId") String approvalId) {
        ApprovalResponse approvalResponse = approvalService.getApprovalById(approvalId);
        return approvalResponse != null ? new ResponseEntity<>(approvalResponse, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<Void> deleteApproval(@PathVariable("approvalId") String approvalId) {
        approvalService.deleteApproval(approvalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
