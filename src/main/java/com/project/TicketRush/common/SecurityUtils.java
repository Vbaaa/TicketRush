package com.project.TicketRush.common;

import org.springframework.security.core.Authentication;

public final class SecurityUtils {
    private SecurityUtils() {}
    public static boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
