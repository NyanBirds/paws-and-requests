package com.codecool.pawsandrequests.security;

import com.codecool.pawsandrequests.model.CustomUserDetails;
import com.codecool.pawsandrequests.service.JwtService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.codecool.pawsandrequests.TestFixtures.EMAIL;
import static com.codecool.pawsandrequests.TestFixtures.details;
import static com.codecool.pawsandrequests.TestFixtures.shelter;
import static com.codecool.pawsandrequests.TestFixtures.shelterUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter")
class JwtAuthenticationFilterTest {

    private static final String TOKEN = "header.payload.signature";

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void doFilter() throws Exception {
        filter.doFilter(request, response, filterChain);
    }

    @Test
    @DisplayName("passes through when no Authorization header is present")
    void passesThroughWithoutHeader() throws Exception {
        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verifyNoInteractions(jwtService, userDetailsService);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("ignores a non Bearer authorization scheme")
    void ignoresOtherScheme() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verifyNoInteractions(jwtService, userDetailsService);
    }

    @Test
    @DisplayName("authenticates a valid bearer token")
    void authenticatesValidToken() throws Exception {
        UserDetails userDetails =
                details(
                        shelterUser(shelter()));
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        when(jwtService.isValid(TOKEN, userDetails)).thenReturn(true);

        doFilter();

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo(EMAIL);
        assertThat(authentication.getPrincipal()).isSameAs(userDetails);
        assertThat(authentication.getCredentials()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("does not authenticate when the token fails validation")
    void doesNotAuthenticateInvalidToken() throws Exception {
        UserDetails userDetails =
                details(
                        shelterUser(shelter()));
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        when(jwtService.isValid(TOKEN, userDetails)).thenReturn(false);

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("does not authenticate when the subject cannot be resolved")
    void doesNotAuthenticateNullSubject() throws Exception {
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(null);

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verifyNoInteractions(userDetailsService);
    }

    @Test
    @DisplayName("leaves the request unauthenticated when the lookup fails")
    void unauthenticatedWhenLookupFails() throws Exception {
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenThrow(new UsernameNotFoundException("gone"));

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("never loads a user when the context is already populated")
    void doesNotTouchServicesWhenAlreadyAuthenticated() throws Exception {
        Authentication existing = new UsernamePasswordAuthenticationToken(
                "stale", null, java.util.List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(existing);
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isSameAs(existing);
        verify(userDetailsService, never())
                .loadUserByUsername(anyString());
    }

    @Test
    @DisplayName("continues the chain when the token cannot be parsed")
    void continuesWhenTokenUnparseable() throws Exception {
        request.addHeader("Authorization", "Bearer garbage");
        when(jwtService.extractUsername("garbage"))
                .thenThrow(new IllegalArgumentException("malformed"));

        doFilter();

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("only looks up the user once the token parses")
    void parsesBeforeLookup() throws Exception {
        CustomUserDetails userDetails =
                details(
                        shelterUser(shelter()));
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        when(jwtService.isValid(TOKEN, userDetails)).thenReturn(true);

        doFilter();

        verify(jwtService).extractUsername(TOKEN);
        verify(jwtService).isValid(TOKEN, userDetails);
        verify(userDetailsService, times(1))
                .loadUserByUsername(EMAIL);
    }

    @Test
    @DisplayName("strips the Bearer prefix before parsing")
    void stripsBearerPrefix() throws Exception {
        UserDetails userDetails =
                details(
                        shelterUser(shelter()));
        request.addHeader("Authorization", "Bearer " + TOKEN);
        when(jwtService.extractUsername(TOKEN)).thenReturn(EMAIL);
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        when(jwtService.isValid(any(), any())).thenReturn(true);

        doFilter();

        verify(jwtService).extractUsername(TOKEN);
    }
}
