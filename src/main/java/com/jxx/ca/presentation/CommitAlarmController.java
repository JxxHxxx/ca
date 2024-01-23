package com.jxx.ca.presentation;

import com.jxx.ca.application.CommitAlarmService;
import com.jxx.cadto.request.UserEnrollForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommitAlarmController {

    private final CommitAlarmService commitAlarmService;

    @PostMapping("/users/enroll")
    public void enrollUser(@RequestBody UserEnrollForm form) {
        commitAlarmService.enrollMember(form);
    }

    // 1.
    @GetMapping("/users/recently-repo")
    public void searchRecentlyPushedRepo() {
        commitAlarmService.searchRecentlyPushedRepo();
    }

    // 2.커밋 여부 확인
    @GetMapping("/check-commit")
    public void checkCommitDone(@RequestParam("since") String sinceTime) {
        commitAlarmService.checkTodayCommit(sinceTime);
    }

    @GetMapping("/notifications")
    public void notificate() {
        commitAlarmService.notificate();
    }

    // 12시 되면 today, done 초기화 로직

}
