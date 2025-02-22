package likelion13.page.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13.page.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        System.out.println("1");
        String path = request.getRequestURI();
//        if (path.equals("/login") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs")) {
//            filterChain.doFilter(request, response);
////            System.out.println("2");
//            return;
//        }
        if (!path.startsWith("/admin") && !path.startsWith("/item-rent") && !path.startsWith("/mypage") && !path.startsWith("/joined-list") && !path.startsWith("/changeIconClub")) {
            // 경로가 /admin/** 또는 /item-rent/**가 아닌 경우, 필터를 통과시킴
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtUtility.resolveToken(request);
//            System.out.println("3");
            if (token != null && jwtUtility.validateToken(token)) {
                Authentication auth = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            System.out.println("e: " + e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);    // 다음 필터로 요청과 응답을 전달
    }

    // 인증객체 생성
    private Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtUtility.getStudentId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
