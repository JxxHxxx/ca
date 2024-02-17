package com.jxx.ca.batch.job.commit.processor;

import com.jxx.ca.batch.job.commit.model.RenewRepoModel;
import com.jxx.ca.domain.GithubRecentRepoFinderFunction;
import com.jxx.ca.domain.TodayCommitTracer;
import com.jxx.ca.github.authorization.GithubTokenGenerator;
import com.jxx.ca.github.authorization.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class RenewRepoProcessor implements ItemProcessor<RenewRepoModel, RenewRepoModel> {

    private final TokenGenerator tokenGenerator = new GithubTokenGenerator();

    @Override
    public RenewRepoModel process(RenewRepoModel item) throws Exception {
        GithubRecentRepoFinderFunction function = new GithubRecentRepoFinderFunction(tokenGenerator);
        String renewRepoName = function.apply(item.getGithubName());

        boolean repoNameChanged = TodayCommitTracer.checkRepoNameChanged(item.getRecentlyPushedRepoName(), renewRepoName, item.getGithubName());

        return repoNameChanged ? item : null;
    }
}
