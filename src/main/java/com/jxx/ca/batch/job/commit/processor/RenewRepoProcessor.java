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

        if (Objects.isNull(renewRepoName)) {
            log.info("사용자:{} 리포짓토리 이름을 찾을 수 없습니다. write 대상이 아닙니다.", item.getGithubName());
            return null;
        }

        log.info("사용자:{} 갱신된 레포짓토리 명:{}", item.getGithubName(), renewRepoName);
        item.renewRepoName(renewRepoName);
        return item;
    }
}
