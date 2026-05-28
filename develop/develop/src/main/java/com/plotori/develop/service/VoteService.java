package com.plotori.develop.service;


import com.plotori.develop.domain.entity.Submission;
import com.plotori.develop.domain.entity.User;
import com.plotori.develop.domain.entity.Vote;
import com.plotori.develop.repository.SubmissionRepository;
import com.plotori.develop.repository.UserRepository;
import com.plotori.develop.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public void vote(Long submissionId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (voteRepository.existsByUserIdAndSubmissionId(user.getId(), submissionId)) {
            throw new RuntimeException("You have already upvoted this submission");
        }

        Vote vote = Vote.builder()
                .user(user)
                .submission(submission)
                .build();

        voteRepository.save(vote);
    }

    @Transactional(readOnly = true)
    public long getVoteCount(Long submissionId) {
        return voteRepository.countBySubmissionId(submissionId);
    }
}
