package com.example.Learn.LearnOne;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        logger.info("Authentication successful for user: {}", authentication.getName());

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        logger.info("User roles: {}", roles);

        if (roles.contains("ROLE_ADMIN")) {
            logger.info("Redirecting ADMIN to /admin-dashboard");
            response.sendRedirect("/admin-dashboard");
        } else if (roles.contains("ROLE_WELFARE_OFFICER")) {
            logger.info("Redirecting WELFARE_OFFICER to /welfare-dashboard");
            response.sendRedirect("/welfare-dashboard");
        } else if (roles.contains("ROLE_CONSTITUENT")) {
            logger.info("Redirecting CONSTITUENT to /constituent-dashboard");
            response.sendRedirect("/constituent-dashboard");
        } else {
            logger.info("Redirecting USER to /dashboard");
            response.sendRedirect("/user-dashboard");
        }
    }
}
