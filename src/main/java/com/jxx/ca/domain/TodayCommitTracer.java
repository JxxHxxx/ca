package com.jxx.ca.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Getter(AccessLevel.PROTECTED) // 외부에서는 GETTER 를 호출하지 마시오
public class TodayCommitTracer {

    private List<GithubMember> existingMembers = new ArrayList<>();
    private List<GithubMember> newMembers = new ArrayList<>();

    public TodayCommitTracer(List<GithubMember> githubMembers) {
        filterMembers(githubMembers);
    }

    private void filterMembers(List<GithubMember> githubMembers) {
        for (GithubMember githubMember : githubMembers) {
            if (githubMember.hasTodayCommit()) {
                existingMembers.add(githubMember);
            } else {
                newMembers.add(githubMember);
            }
        }
    }

    public List<TodayCommit> createTodayCommit(Function<String, String> function) {
        return newMembers.stream()
                .map(newMember -> {
                    String repoName = function.apply(newMember.getGithubName());
                    log.info("사용자:{} 신규 등록 성공 여부:{}", newMember.getGithubName(), repoName == null ? "N":"Y");
                    return repoName == null ? null : new TodayCommit(newMember, repoName);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public void renewTodayCommit(Function<String, String> function) {
        for (GithubMember existingMember : existingMembers) {
            TodayCommit todayCommit = existingMember.getTodayCommit();
            String renewRepoName = function.apply(existingMember.getGithubName());

            if (todayCommit.isSameRecentlyPushedRepoName(renewRepoName)) {
                log.info("사용자:{} 갱신된 리포짓토리가 기존에 갱신된 리포짓토리와 동일합니다. write 대상이 아닙니다.",
                        existingMember.getGithubName());
            }
            else if (Objects.isNull(renewRepoName)) {
                log.info("사용자:{} 갱신된 리포짓토리를 불러올 수 없습니다.", existingMember.getGithubName());
            }
            else {
                log.info("사용자:{} 리포짓토리가 갱신됩니다. before:{} -> after:{}",
                        existingMember.getGithubName(), todayCommit.getRecentlyPushedRepoName(), renewRepoName);
                todayCommit.updateRecentlyPushedRepoName(renewRepoName); // dirty checking
            }
        }
    }
}
