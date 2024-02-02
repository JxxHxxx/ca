package com.jxx.ca.github.api;

import java.util.List;

public interface CommitHistoryApiAdapter<T> {

    List body(T request, String sinceTime);
}
