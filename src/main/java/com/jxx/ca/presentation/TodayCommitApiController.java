package com.jxx.ca.presentation;

import com.jxx.ca.application.CommitManagerService;
import com.jxx.ca.dto.response.TodayCommitResponse;
import com.jxx.ca.dto.response.TodayCommitResult;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TodayCommitApiController {

    private final CommitManagerService commitManagerService;

    @PostMapping("/github-members")
    public void enrollUsers(@RequestBody List<UserEnrollForm> form) {
        commitManagerService.enrollMembers(form);
    }

    @PatchMapping("/today-commits/renew-repo-name")
    public void renewRepoNames() {
        commitManagerService.renewRepoNameAllUsers();
    }

    @PatchMapping("/today-commits/check-commit")
    public void checkCommitDone(@RequestParam("since") String sinceTime) {
        commitManagerService.checkTodayCommits(sinceTime);
    }

    @PatchMapping("/today-commits/{today-commit-pk}/check-commit")
    public void checkCommitDone(@PathVariable("today-commit-pk") Long todayCommitPk, @RequestParam("since") String sinceTime) {
        commitManagerService.checkTodayCommit(todayCommitPk, sinceTime);
    }

    @GetMapping("/today-commits")
    public ResponseEntity<TodayCommitResult> getTodayCommit(@RequestParam(value = "namePattern", defaultValue = "") String namePattern) {
        List<TodayCommitResponse> response = commitManagerService.findTodayCommit(namePattern);
        return ResponseEntity.ok(new TodayCommitResult(HttpStatus.OK.value(), "당일 커밋 조회", LocalDate.now(), response));
    }

}
