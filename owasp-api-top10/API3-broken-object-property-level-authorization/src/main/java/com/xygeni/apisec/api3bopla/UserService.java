package com.xygeni.apisec.api3bopla;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final Map<String, User> bySsn = new ConcurrentHashMap<>();

    public UserProfileResponse findBySsn(String ssn) {
        User user = bySsn.getOrDefault(ssn, new User());
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), ssn);
    }

    public UserProfileResponse update(UserProfileUpdateRequest update) {
        User user = new User();
        user.setName(update.getName());
        user.setEmail(update.getEmail());
        user.setRole(update.getRole());
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getSsn());
    }

    public UserProfileResponse secureFindBySsn(String ssn) {
        User user = bySsn.getOrDefault(ssn, new User());
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), ssn);
    }

    public UserSummaryResponse summary(String id) {
        return new UserSummaryResponse(id, "Jane Doe");
    }

    public UserProfileResponse safeUpdate(String id, UserProfileSafeUpdateRequest update) {
        User user = new User();
        user.setId(id);
        user.setName(update.getName());
        user.setEmail(update.getEmail());
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getSsn());
    }
}
