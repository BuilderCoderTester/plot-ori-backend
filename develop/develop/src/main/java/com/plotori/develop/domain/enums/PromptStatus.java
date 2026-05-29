package com.plotori.develop.domain.enums;

public enum PromptStatus {
    VOTING,      // Saturday-Sunday: community votes on next week's text
    ACTIVE,      // Monday-Friday: prompt is live, submissions accepted
    REVIEWING,   // Saturday: admin reviews, selects featured
    ARCHIVED     // Past prompt, submissions locked
}
