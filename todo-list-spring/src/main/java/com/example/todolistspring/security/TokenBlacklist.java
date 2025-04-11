package com.example.todolistspring.security;

import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.HashSet;

@Component
public class TokenBlacklist {
    private final Set<String> blacklist = new HashSet<>();

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean contains(String token) {
        return blacklist.contains(token);
    }
}
