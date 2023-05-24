package com.service.surveyservice.domain.survey.dao;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.dto.QMemberSurveyDTO_MemberSurveyInfoDTO;
import com.service.surveyservice.domain.survey.dto.QSurveyDTO_SurveyInfoDTO;
import com.service.surveyservice.domain.survey.model.QMemberSurvey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;
import static com.service.surveyservice.domain.survey.model.QMemberSurvey.*;

@Slf4j
@RequiredArgsConstructor
public class MemberSurveyCustomRepositoryImpl implements MemberSurveyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 철웅오빠 - 참여
    @Override
    public Page<MemberSurveyInfoDTO> findByMemberIdWithPage(Long memberId, Pageable pageable) {
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(memberSurvey.id.count())
                .from(memberSurvey)
                .where(memberSurvey.member.id.eq(memberId));

        List<MemberSurveyInfoDTO> infoList = getInfoList(memberId);
        return PageableExecutionUtils.getPage(infoList, pageable, countQuery::fetchOne);
    }

    public List<MemberSurveyInfoDTO> getInfoList(Long id) {
        return jpaQueryFactory
                .select(new QMemberSurveyDTO_MemberSurveyInfoDTO(memberSurvey.id, memberSurvey.member.id, memberSurvey.survey.id))
                .from(memberSurvey)
                .where(memberSurvey.member.id.eq(id))
                .fetch();
    }


}
