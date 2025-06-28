package backend.teamble.security;

import backend.teamble.config.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        String uri = request.getRequestURI();

        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                String email = jwtTokenProvider.getEmailFromToken(token);
                log.info("✅ 유효한 토큰입니다. 이메일: {}, URI: {}", email, uri);

                CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("🔐 SecurityContext에 인증 정보 저장 완료");
            } else {
                log.warn("⚠️ 유효하지 않은 토큰입니다. URI: {}", uri);
            }
        } else {
            log.info("🔓 인증 없는 요청 허용됨. URI: {}", uri);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}