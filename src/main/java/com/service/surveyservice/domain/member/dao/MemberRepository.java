package com.service.surveyservice.domain.member.dao;

import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserNameAndNickname(String userName, String nickname);
    Optional<Member> findByUserNameAndNicknameAndEmail(String userName, String nickname, String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query(value = "SELECT m FROM Member m WHERE m.email = :email")
    Member findByEmailForOAuth(@Param("email") String email);
}
