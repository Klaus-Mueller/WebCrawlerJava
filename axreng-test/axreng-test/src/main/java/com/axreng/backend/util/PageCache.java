package com.axreng.backend.util;

import java.util.HashMap;
import java.util.Map;

public class PageCache {
    private Map<String, String> cache;

    public PageCache() {
        this.cache = new HashMap<>();
    }

    public synchronized void put(String url, String content) {
        cache.put(url, content);
    }

    public synchronized String get(String url) {
        return cache.get(url);
    }

    public synchronized boolean contains(String url) {
        return cache.containsKey(url);
    }
}
