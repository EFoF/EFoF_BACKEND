package com.service.surveyservice.domain.survey.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.service.surveyservice.domain.answer.model.QAnswer;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.survey.dto.QSurveyDTO_SurveyInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.service.surveyservice.domain.answer.model.QAnswer.*;
import static com.service.surveyservice.domain.question.dto.QuestionDTO.*;
import static com.service.surveyservice.domain.question.dto.QuestionOptionDTO.*;
import static com.service.surveyservice.domain.question.model.QQuestion.question;
import static com.service.surveyservice.domain.question.model.QQuestionOption.questionOption;
import static com.service.surveyservice.domain.section.model.QSection.section;
import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;
import static com.service.surveyservice.domain.survey.model.QSurvey.*;

@Slf4j
@RequiredArgsConstructor
public class SurveyCustomRepositoryImpl implements SurveyCustomRepository{


    private final EntityManager em;
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


    @Override
    public SurveySectionQueryDTO findSurveyBySurveyId(Long survey_id) {

        SurveySectionQueryDTO surveySectionQueryDTO = findSurveyInfo(survey_id);
        List<SectionDTO.SectionQuestionQueryDto> surveySectionInfo = findSurveySectionInfo(survey_id);
        List<Long> sectionIdList = surveySectionInfo.stream()
                .map(SectionDTO.SectionQuestionQueryDto::getId)
                .collect(Collectors.toList());
        Map<Long, List<QuestionQueryDto>> questionInfo = findQuestionInfo(sectionIdList);

        surveySectionInfo.forEach(sS ->
        {
            if (questionInfo.containsKey(sS.getId())) {
                sS.setQuestionList(questionInfo.get(sS.getId()));
            }else {
                // 키가 존재하지 않는 경우에 대한 처리
                // 예를 들어, 빈 리스트로 설정하거나 기본값을 할당할 수 있습니다.
                sS.setQuestionList(new ArrayList<>()); // 빈 리스트로 설정
            }
        });
        surveySectionQueryDTO.setSectionList(surveySectionInfo);


        return surveySectionQueryDTO;
    }

    private SurveySectionQueryDTO findSurveyInfo(Long survey_id) {
        SurveySectionQueryDTO surveySectionQueryDTO = queryFactory
                .select(Projections.constructor(SurveySectionQueryDTO.class,
                        survey.id,
                        survey.title,
                        survey.description,
                        survey.sImageURL,
                        survey.fontColor,
                        survey.bgColor,
                        survey.btColor,
                        survey.openDate,
                        survey.expireDate
               ) )
                .from(survey)
                .where(survey.id.eq(survey_id)).fetchOne();
        return surveySectionQueryDTO;
    }

    private  List<SectionDTO.SectionQuestionQueryDto> findSurveySectionInfo(Long survey_id) {
        return queryFactory.select(Projections.constructor(SectionDTO.SectionQuestionQueryDto.class,
                section.id,
                section.parentSection.id,
                section.questionOrder)).from(section).where(section.survey.id.eq(survey_id)).fetch();
    }


    private Map<Long, List<QuestionQueryDto>> findQuestionInfo(List<Long> sectionIdList) {
        List<QuestionQueryDto> questionList = queryFactory.select(Projections.constructor(QuestionQueryDto.class,
                        question.id,
                        question.questionType,
                        question.questionText,
                        question.isNecessary,
                        question.hasImage,
                        question.section.id)).
                from(question).
                where(question.section.id.in(sectionIdList)).fetch();

        List<Long> questionIdList = questionList.stream()
                .map(QuestionQueryDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<QuestionOptionQueryDto>> questionOptionInfo = findQuestionOptionInfo(questionIdList);

        questionList.forEach(ql -> ql.setOptions(questionOptionInfo.get(ql.getId())));

        Map<Long, List<QuestionQueryDto>> questionMap = questionList.stream()
                .collect(Collectors.groupingBy(questionListDto -> questionListDto.getSectionId()));

        return questionMap;
    }

    private Map<Long, List<QuestionOptionQueryDto>> findQuestionOptionInfo(List<Long> questionIdList) {
        List<QuestionOptionQueryDto> questionOptionList = queryFactory.select(Projections.constructor(QuestionOptionQueryDto.class,
                        questionOption.id,
                        questionOption.optionText,
                        questionOption.questionOptionImg,
                        questionOption.nextSection.id,
                        questionOption.question.id)).
                from(questionOption).
                where(questionOption.question.id.in(questionIdList)).fetch();

        Map<Long, List<QuestionOptionQueryDto>> questionOptionMap = questionOptionList.stream()
                .collect(Collectors.groupingBy(questionOptionListDto -> questionOptionListDto.getQuestionId()));

        return questionOptionMap;
    }


    private List<SurveyInfoDTO> findSurveyInfoDTOByAuthorIdQuery(Long authorId) {
        List<SurveyInfoDTO> results = queryFactory
                .select(new QSurveyDTO_SurveyInfoDTO(survey.title, survey.description, survey.author.id, survey.sImageURL, survey.releaseStatus))
                .from(survey)
                .where(survey.author.id.eq(authorId))
                .fetch();

        return results;
    }

    @Override
    public SurveySectionQueryDTO findSurveyBySurveyIdWithAnswer (Long survey_id) {

        SurveySectionQueryDTO surveySectionQueryDTO = findSurveyInfo(survey_id);
        List<SectionDTO.SectionQuestionQueryDto> surveySectionInfo = findSurveySectionInfo(survey_id);
        List<Long> sectionIdList = surveySectionInfo.stream()
                .map(SectionDTO.SectionQuestionQueryDto::getId)
                .collect(Collectors.toList());
        Map<Long, List<QuestionQueryDto>> questionInfo = findQuestionInfoWithAnswer(sectionIdList);

        surveySectionInfo.forEach(sS ->
        {
            if (questionInfo.containsKey(sS.getId())) {
                sS.setQuestionList(questionInfo.get(sS.getId()));
            }else {
                // 키가 존재하지 않는 경우에 대한 처리
                // 예를 들어, 빈 리스트로 설정하거나 기본값을 할당할 수 있습니다.
                sS.setQuestionList(new ArrayList<>()); // 빈 리스트로 설정
            }
        });
        surveySectionQueryDTO.setSectionList(surveySectionInfo);


        return surveySectionQueryDTO;
    }

    private Map<Long, List<QuestionQueryDto>> findQuestionInfoWithAnswer(List<Long> sectionIdList) {
        List<QuestionQueryDto> questionList = queryFactory.select(Projections.constructor(QuestionQueryDto.class,
                question.id,
                question.questionType,
                question.questionText,
                question.isNecessary,
                question.hasImage,
                question.section.id)).
                from(question).
                where(question.section.id.in(sectionIdList)).fetch();

        List<Long> questionIdList = questionList.stream()
                .map(QuestionQueryDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<QuestionOptionQueryDto>> questionOptionInfo = findQuestionOptionInfo(questionIdList);
        Map<Long, List<QuestionAnswersQueryDto>> questionAnswers = findQuestionAnswers(questionIdList);

        questionList.forEach(ql -> ql.setOptions(questionOptionInfo.get(ql.getId())));

        questionList.forEach(element -> {
            // nullPointer 발생
            for (QuestionAnswersQueryDto questionAnswersQueryDto : questionAnswers.get(element.getId())) {
                if(questionAnswersQueryDto.getNarrativeAnswer() == null) {
                    // 객관식 답변의 경우 리스트에 추가해줌
                    element.addAnswerToList(questionAnswersQueryDto.getMarkedOptionId());
                } else {
                    // 조관식 답변의 경우 narrative Answer에 추가해줌
                    element.setNarrativeAnswer(questionAnswersQueryDto.getNarrativeAnswer());
                }
            }
        });

        Map<Long, List<QuestionQueryDto>> questionMap = questionList.stream()
                .collect(Collectors.groupingBy(questionListDto -> questionListDto.getSectionId()));

        return questionMap;
    }

    // 사실상 AnswerCustomRepository로 가야하는데, 가독성을 위해 여기서 다루겠음
    private Map<Long, List<QuestionAnswersQueryDto>> findQuestionAnswers(List<Long> questionIdList) {
        // id로 조회, group by로 정렬
        List<QuestionAnswersQueryDto> totalQueryResult = queryFactory.select(Projections.constructor(QuestionAnswersQueryDto.class,
                answer.question.id,
                answer.answerSentence,
                answer.questionOption.id
        )).from(answer).where(answer.question.id.in(questionIdList)).fetch();

        Map<Long, List<QuestionAnswersQueryDto>> collect
                = totalQueryResult.stream().collect(Collectors.groupingBy(field -> field.getId()));

        return collect;
    }

}
