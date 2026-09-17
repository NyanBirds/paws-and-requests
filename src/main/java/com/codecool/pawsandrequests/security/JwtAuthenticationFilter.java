package com.codecool.pawsandrequests.security;

import com.codecool.pawsandrequests.service.JwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
// REVIEW(noob): implement OncePerRequestFilter rather than the raw servlet Filter. A plain Filter can run more than once per request (forwards, async dispatches, error dispatches), and you then redo the JWT parse and the user lookup each time. OncePerRequestFilter is the Spring Security idiom and gives you doFilterInternal plus shouldNotFilter.
public class JwtAuthenticationFilter implements Filter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            final JwtService service,
            final UserDetailsService udService
    ) {
        this.jwtService = service;
        this.userDetailsService = udService;
    }


    @Override
    public final void doFilter(
            @NonNull final ServletRequest servletRequest,
            @NonNull final ServletResponse servletResponse,
            @NonNull final FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String header = request.getHeader(HEADER);

        if (header == null || !header.startsWith(PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIX.length());

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder
                    .getContext().getAuthentication() == null) {

                // REVIEW(efficiency): every single authenticated request hits the database to load the user. That is acceptable, but it is why people put the role (and here the orgNr) in the token claims. Worth knowing you made that trade.
                var userDetails = userDetailsService
                        .loadUserByUsername(username);

                if (jwtService.isValid(token, userDetails)) {
                    var authentication =
                            new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                    );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }
        // REVIEW(sec): catching bare Exception and carrying on means a tampered, expired or malformed token is silently indistinguishable from sending no token at all. The request continues as anonymous and, with .permitAll() upstairs, usually still succeeds. At minimum log it at debug so it is diagnosable, and catch JwtException specifically rather than Exception.
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
