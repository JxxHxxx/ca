package com.jxx.ca.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TodayCommitResult<R> {
    private final int status;
    private final String message;
    private final LocalDate today;
    private R data;
}
