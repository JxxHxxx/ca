package com.jxx.ca.presentation;

import com.jxx.ca.application.CommitManagerService;
import com.jxx.ca.dto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TodayCommitApiController {

    private final CommitManagerService commitManagerService;

    @PostMapping("/users")
    public void enrollUsers(@RequestBody List<UserEnrollForm> form) {
        commitManagerService.enrollMembers(form);
    }

    @PatchMapping("/commits/renew-repo-name")
    public void renewRepoNames() {
        commitManagerService.renewRepoNameAllUsers();
    }

    // STEP 2
    @PatchMapping("/commits/check-commit")
    public void checkCommitDone(@RequestParam("since") String sinceTime) {
        commitManagerService.checkTodayCommit(sinceTime);
    }

    // STEP 3
    @GetMapping("/notifications")
    public void notificate() {
        commitManagerService.notificate();
    }

    // 12시 되면 today, done 초기화 로직

}
