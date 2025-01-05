package greeny.backend.config.security;

import greeny.backend.domain.member.entity.GeneralMemberRepository;
import greeny.backend.domain.member.entity.Member;
import greeny.backend.domain.member.entity.MemberRepository;
import greeny.backend.exception.situation.member.GeneralMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final GeneralMemberRepository generalMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(member -> createUserDetails(
                        member,
                        generalMemberRepository.findByMemberId(member.getId()).orElseThrow(GeneralMemberNotFoundException::new).getPassword()
                )).orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member, String password) {
        return User.builder()
                .username(member.getEmail())
                .password(password)
                .authorities(new SimpleGrantedAuthority(member.getRole().toString()))
                .build();
    }
}