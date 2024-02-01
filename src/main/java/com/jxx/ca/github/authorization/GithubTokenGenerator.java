package com.jxx.ca.github.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubTokenGenerator implements TokenGenerator {

    @Value("${github.auth-header.token}")
    private String githubToken;

    @Override
    public String receive() {
        return githubToken;
    }
}
