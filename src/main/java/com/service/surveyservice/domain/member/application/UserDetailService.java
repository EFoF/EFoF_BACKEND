package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load by username : " + username);
        Optional<Member> byEmail = memberRepository.findByEmail(username);
        if(byEmail.isPresent()) {
            return createUserDetails(byEmail.get());
        } else {
            throw new UsernameNotFoundException(username + " 사용자가 데이터베이스에 존재하지 않습니다.");
        }
    }

    // 어드민 유저는 일단은 게획에 없기 때문에, 권한은 빈 값으로 주고 후에 고도화 과정에서 다시 고려해보겠다.
    private UserDetails createUserDetails(Member member) {
        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.emptyList()
        );
    }

}
