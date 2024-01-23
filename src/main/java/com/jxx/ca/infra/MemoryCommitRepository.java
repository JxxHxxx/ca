package com.jxx.ca.infra;

import com.jxx.ca.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryCommitRepository {

    private static Map<String, Member> store = new ConcurrentHashMap<>();

    public void save(String username, String repoName) {
        Member member = new Member(username, repoName, false);
        store.put(username, member);
    }

    public void updateLastPushedRepo(String username, String repoName) {
        Member member = store.get(username);
        member.setLastUpdateRepoName(repoName);

        store.put(username, member);
    }

    public void update(Member member) {
        store.put(member.getName(), member);
    }

    public List<Member> getAll() {
        List<Member> members = new ArrayList<>();
        for (String username : store.keySet()) {
            Member member = store.get(username);
            members.add(member);
        }
        return members;
    }
}
