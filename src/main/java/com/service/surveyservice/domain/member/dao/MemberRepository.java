package com.service.surveyservice.domain.member.dao;

import com.service.surveyservice.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserNameAndNickname(String userName, String nickname);
    Optional<Member> findByUserNameAndNicknameAndEmail(String userName, String nickname, String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query(value = "SELECT m FROM Member m WHERE m.email = :email", nativeQuery = true)
    Member findByEmailForOAuth(String email);
}
