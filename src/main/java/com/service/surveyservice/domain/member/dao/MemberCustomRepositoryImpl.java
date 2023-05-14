package com.service.surveyservice.domain.member.dao;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.member.dto.QMemberDTO_MemberDetail;
import com.service.surveyservice.domain.member.dto.QMemberDTO_MemberTokenPublishConfirmDTO;
import com.service.surveyservice.domain.member.model.QMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.domain.member.model.QMember.*;

@Slf4j
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public MemberDetail getMemberDetail(Long userId) {
        return queryFactory
                .select(new QMemberDTO_MemberDetail(member.id, member.userName, member.nickname, member.email))
                .from(member)
                .where(member.id.eq(userId)).fetchOne();
    }

    @Override
    public Optional<MemberDetail> getMemberDetailOptional(Long userId) {
        MemberDetail memberDetail = queryFactory
                .select(new QMemberDTO_MemberDetail(member.id, member.userName, member.nickname, member.email))
                .from(member)
                .where(member.id.eq(userId)).fetchOne();

        return Optional.ofNullable(memberDetail);
    }

    @Override
    public Optional<MemberTokenPublishConfirmDTO> getMemberTokenPublishConfirm(Long userId) {
        MemberTokenPublishConfirmDTO memberTokenPublishConfirmDTO = queryFactory
                .select(new QMemberDTO_MemberTokenPublishConfirmDTO(member.memberLoginType))
                .from(member)
                .where(member.id.eq(userId)).fetchOne();
        return Optional.ofNullable(memberTokenPublishConfirmDTO);
    }
}
