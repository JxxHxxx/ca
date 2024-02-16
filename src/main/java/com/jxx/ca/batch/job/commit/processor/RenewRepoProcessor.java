package com.jxx.ca.batch.job.commit.processor;

import com.jxx.ca.batch.job.commit.model.RenewRepoModel;
import com.jxx.ca.domain.GithubRecentRepoFinderFunction;
import com.jxx.ca.github.authorization.GithubTokenGenerator;
import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.Objects;

@Slf4j
public class RenewRepoProcessor implements ItemProcessor<RenewRepoModel, RenewRepoModel> {

    private final TokenGenerator tokenGenerator = new GithubTokenGenerator();

    @Override
    public RenewRepoModel process(RenewRepoModel item) throws Exception {
        GithubRecentRepoFinderFunction function = new GithubRecentRepoFinderFunction(tokenGenerator);
        String renewRepoName = function.apply(item.getGithubName());

        // repoName 을 못 찾았을 경우 null , 기존과 동일한 경우 null, 새롭게 갱신된 경우에만 item return
        if (Objects.isNull(renewRepoName)) {
            log.info("사용자:{} 리포짓토리 이름을 찾을 수 없습니다. write 대상이 아닙니다.", item.getGithubName());
            return null;
        }

        if (item.isSameRecentlyPushedRepoName(renewRepoName)) {
            log.info("사용자:{} renewRepoName:{} RecentlyPushedRepoName:{} write 대상이 아닙니다.",
                    item.getGithubName(), renewRepoName, item.getRecentlyPushedRepoName());
            return null;
        }

        log.info("사용자:{} renewRepoName:{} RecentlyPushedRepoName:{} write 대상입니다.",
                item.getGithubName(), renewRepoName, item.getRecentlyPushedRepoName());
        item.renewRepoName(renewRepoName);
        return item;
    }
}
