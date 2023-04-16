package com.service.surveyservice.domain.survey.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.surveyservice.domain.survey.dto.QSurveyDTO_SurveyInfoDTO;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.QSurvey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;
import static com.service.surveyservice.domain.survey.model.QSurvey.*;

@Slf4j
@RequiredArgsConstructor
public class SurveyCustomRepositoryImpl implements SurveyCustomRepository{

    private final JPAQueryFactory queryFactory;

    // 페이지네이션을 구현할 때 최적화를 위해서 카운트 쿼리와 실제 쿼리를 분리해서 2번 보낸다.
    @Override
    public Page<SurveyInfoDTO> findSurveyInfoDTOByAuthorId(Long authorId, Pageable pageable) {
        // 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(survey.id.count())
                .from(survey)
                .where(survey.author.id.eq(authorId));

        List<SurveyInfoDTO> results = findSurveyInfoDTOByAuthorIdQuery(authorId);
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private List<SurveyInfoDTO> findSurveyInfoDTOByAuthorIdQuery(Long authorId) {
        List<SurveyInfoDTO> results = queryFactory
                .select(new QSurveyDTO_SurveyInfoDTO(survey.title, survey.description, survey.author.id, survey.sImageURL, survey.surveyStatus))
                .from(survey)
                .where(survey.author.id.eq(authorId))
                .fetch();

        return results;
    }
}
