package com.plotori.develop.service;


import com.plotori.develop.domain.entity.Submission;
import com.plotori.develop.domain.entity.Text;
import com.plotori.develop.domain.entity.User;
import com.plotori.develop.domain.enums.SubmissionStatus;
import com.plotori.develop.dto.request.SubmissionRequest;
import com.plotori.develop.dto.response.SubmissionResponse;
import com.plotori.develop.repository.SubmissionRepository;
import com.plotori.develop.repository.TextRepository;
import com.plotori.develop.repository.UserRepository;
import com.plotori.develop.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final TextRepository textRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Text text = textRepository.findById(request.getTextId())
                .orElseThrow(() -> new RuntimeException("Text not found"));

        Submission submission = Submission.builder()
                .user(user)
                .text(text)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .status(request.getStatus() != null ? request.getStatus() : SubmissionStatus.DRAFT)
                .build();

        Submission saved = submissionRepository.save(submission);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public SubmissionResponse getSubmissionById(Long id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        return mapToResponse(submission);
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByText(Long textId) {
        return submissionRepository.findByTextIdAndStatus(textId, SubmissionStatus.SUBMITTED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getWeeklySubmissions() {
        return submissionRepository.findByStatus(SubmissionStatus.SUBMITTED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getMySubmissions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return submissionRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SubmissionResponse mapToResponse(Submission submission) {
        Long voteCount = voteRepository.countBySubmissionId(submission.getId());

        return SubmissionResponse.builder()
                .id(submission.getId())
                .authorUsername(submission.getUser().getUsername())
                .textId(submission.getText().getId())
                .textTitle(submission.getText().getTitle())
                .type(submission.getType())
                .title(submission.getTitle())
                .content(submission.getContent())
                .status(submission.getStatus())
                .createdAt(submission.getCreatedAt())
                .updatedAt(submission.getUpdatedAt())
                .voteCount(voteCount.intValue())
                .build();
    }
}
