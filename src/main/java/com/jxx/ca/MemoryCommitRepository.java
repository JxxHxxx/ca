package com.jxx.ca;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class MemoryCommitRepository {

    private static Map<String, String> store = new ConcurrentHashMap<>();

    public void save(String username, String repoName) {
        store.put(username, repoName);
    }

    public String getRepoName(String username) {
       return store.get(username);
    }

    public List<String> getAll() {
        List<String> repoNames = new ArrayList<>();
        for (String username : store.keySet()) {
            String repoName = store.get(username);
            repoNames.add(repoName);
        }
        return repoNames;
    }
}
