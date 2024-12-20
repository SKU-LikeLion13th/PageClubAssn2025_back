package likelion13.page.service;

import likelion13.page.domain.Member;
import likelion13.page.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        Member member = memberRepository.findByStudentId(studentId);
        Set<GrantedAuthority> grantedAuthority = new HashSet<>();

        if (member != null) {
            if (member.getRole().name().equals("ROLE_ADMIN")) {
                grantedAuthority.add(new SimpleGrantedAuthority(member.getRole().name()));
                grantedAuthority.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
            } else {
                grantedAuthority.add(new SimpleGrantedAuthority(member.getRole().name()));
            }

            return new User(member.getStudentId(), member.getName(), grantedAuthority);
        } else {
            throw new UsernameNotFoundException("학번을 찾을 수 없습니다." + studentId);
        }
    }
}