package com.jxx.ca.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodayCommitInformation {

    private final String githubName;
    private final boolean done;
}
