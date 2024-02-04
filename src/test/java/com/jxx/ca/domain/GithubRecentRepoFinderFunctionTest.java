package com.jxx.ca.domain;

import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class GithubRecentRepoFinderFunctionTest {

    @Autowired
    @Qualifier("githubTokenGenerator")
    TokenGenerator tokenGenerator;

    @DisplayName("GITHUB API 사용해서 가장 최근 업데이트 된 리포짓토리 이름을 가져온다." +
            "GITHUB API RATE LIMIT 으로 기본 Disable 해둔다. 필요 시 수동으로 호출해보도록 한다.")
    @Disabled
    @Test
    void find_github_recent_repo() {
        String githubMemberName = "jxxHxxx";
        GithubRecentRepoFinderFunction recentRepoFinderFunction = new GithubRecentRepoFinderFunction(tokenGenerator);

        String recentUpdateRepoName = recentRepoFinderFunction.apply(githubMemberName);

        log.info("githubMemberName : {} recentUpdateRepoName : {}", githubMemberName, recentUpdateRepoName);
    }
}