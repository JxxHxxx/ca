package com.jxx.ca.github.api;

import java.util.List;

public interface GithubRepoCommitHistoryApiAdapter {
    /**
     * 커밋 내역을 확인하는 API
     * @param username : 깃헙 Nickname
     * @param repoName : 커밋 내역을 확인할 리포짓토리
     * @param sinceTime : 일시, ex) 2024-02-17T00:00:00Z 일 경우 2024년 2월 17일 00시 이후의 커밋 내역을 확인
     * @return api 호출에 대한 Response Body
     */

    List request(String username, String repoName, String sinceTime);

}
