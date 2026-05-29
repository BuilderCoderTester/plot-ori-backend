package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.PromptVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromptVoteRepository extends JpaRepository<PromptVote, Long> {
    Optional<PromptVote> findByUserIdAndWeeklyPromptId(Long userId, Long weeklyPromptId);

    @Query("SELECT pv.votedText.id, COUNT(pv) as voteCount FROM PromptVote pv WHERE pv.weeklyPrompt.id = :promptId GROUP BY pv.votedText.id ORDER BY voteCount DESC")
    List<Object[]> countVotesByTextForPrompt(@Param("promptId") Long promptId);

    boolean existsByUserIdAndWeeklyPromptId(Long userId, Long weeklyPromptId);
}
