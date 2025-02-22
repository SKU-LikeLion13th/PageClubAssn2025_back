package likelion13.page.security;

import likelion13.page.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtility jwtUtility;
    private final CustomUserDetailsService customUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable) // spring security 기본 인증 해제 -> jwt 사용
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())       // CORS 설정
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/login", "/agree", "/club-scores/**").permitAll() // Swagger UI와 API 문서화 경로에 대한 접근을 모든 사용자에게 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/item-rent/**").hasRole("MEMBER")
                        .anyRequest().authenticated()
                )
                // 간단한 테스트를 위해 csrf토큰 비활성화
                .csrf((csrf) -> csrf.ignoringRequestMatchers("/**"))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtility, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}