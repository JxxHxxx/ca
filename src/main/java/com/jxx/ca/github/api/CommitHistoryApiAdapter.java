package com.jxx.ca.github.api;

import java.util.List;

public interface CommitHistoryApiAdapter {
    List getResponseBody(String username, String repoName, String sinceTime);

}
