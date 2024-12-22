package likelion13.page.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtility {

//    @Value("${jwt.key}")
//    private String secret = "askqwhrkjweagfjasdfasfdahsjkfhqlwkjfhbasdjkfhlqwkjefhbadskjfbalsdhfvbasdfasdfasdfasdfasdqmjhfvgjasd";

    private final Key secret;

    private static final long expirationTime = 1000 * 60 * 60; // 1시간

    public JwtUtility() {
        this.secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // JWT 생성
    public String generateToken(String memberId) {
        return "Bearer " + Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secret)
                .compact();
    }

    // JWT 클레임 반환
    public String getStudentId(String token) {
        // 토큰 파싱 및 클레임 반환
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 유효한 경우, 클레임 반환
    }

    // JWT 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired");
        } catch (SignatureException e) {
            System.out.println("Invalid signature");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid token");
        }
        return false;
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);        // "Bearer " 문자 이후의 토큰 부분을 반환
        }
        return null;
    }

//    public static void main(String[] args) {
//        JwtUtility jwtUtility = new JwtUtility();
//        String token = jwtUtility.generateToken("00000000");
//        System.out.println("token = " + token);
//        System.out.println("getStudentId(token) = " + jwtUtility.getStudentId(token.substring(7)));
//        System.out.println("asdf = " + jwtUtility.validateToken(token.substring(7)));
//    }
}