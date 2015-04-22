package com.paultech.core.SecurityHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by paulzhang on 22/04/15.
 */
public class RestCsrfFilter extends OncePerRequestFilter {

    private static final String CSRF_TOKEN = "CSRF-TOKEN";
    private static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";
    private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(matchHttpHeader(httpServletRequest)) {
            String csrfHeaderValue = httpServletRequest.getHeader(X_CSRF_TOKEN);
            Cookie[] cookies = httpServletRequest.getCookies();
            String csrfCookieValue = null;

            if(null != cookies) {
                for(Cookie cookie : cookies) {

                    if(cookie.getName().equals(CSRF_TOKEN)) {
                        csrfCookieValue = cookie.getValue();
                    }
                }
            }

            System.out.println(csrfCookieValue);
            System.out.println(csrfHeaderValue);

            if(null == csrfCookieValue || !csrfCookieValue.equals(csrfHeaderValue)) {
                accessDeniedHandler.handle(httpServletRequest,httpServletResponse,new AccessDeniedException("CSRF token is missing or mismatched."));
                return;
            }
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private boolean matchHttpHeader(HttpServletRequest httpServletRequest) {
        Pattern pattern = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
        return !pattern.matcher(httpServletRequest.getMethod()).matches();
    }
}
