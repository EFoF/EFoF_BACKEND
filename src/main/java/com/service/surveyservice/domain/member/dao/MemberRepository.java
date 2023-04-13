package com.service.surveyservice.domain.member.dao;

import com.service.surveyservice.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserNameAndNickname(String userName, String nickname);
    Optional<Member> findByUserNameAndNicknameAndEmail(String userName, String nickname, String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
