package com.plotori.develop.security;
import com.plotori.develop.service.WeeklyPromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromptScheduler {
    private final WeeklyPromptService weeklyPromptService;

    // Saturday 00:00 — Start voting for next week
    @Scheduled(cron = "0 0 0 * * SAT")
    public void scheduleVotingStart() {
        log.info("CRON: Starting voting phase");
        weeklyPromptService.startVotingPhase();
    }

    // Sunday 23:59 — Close voting (handled by frontend deadline, but we can enforce)
    // Monday 00:00 — Activate prompt with winning text
    @Scheduled(cron = "0 0 0 * * MON")
    public void schedulePromptActivation() {
        log.info("CRON: Activating weekly prompt");
        weeklyPromptService.activatePrompt();
    }

    // Friday 23:59 — Close submissions (frontend enforces, backend validates)
    // Saturday 00:00 — Start review phase
    @Scheduled(cron = "0 0 0 * * SAT")
    public void scheduleReviewStart() {
        log.info("CRON: Starting review phase");
        weeklyPromptService.startReviewPhase();
    }

    // Saturday 23:59 — Auto-archive if admin hasn't featured manually
    @Scheduled(cron = "0 0 23 * * SAT")
    public void scheduleAutoArchive() {
        log.info("CRON: Auto-archiving prompt");
        weeklyPromptService.archivePrompt();
    }
}
