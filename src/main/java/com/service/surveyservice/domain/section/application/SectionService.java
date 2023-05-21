package com.service.surveyservice.domain.section.application;

import com.service.surveyservice.domain.question.dao.QuestionOptionRepository;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionType;
import com.service.surveyservice.domain.section.dao.SectionCustomRepository;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.exception.exceptions.SectionNotFoundException;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.service.surveyservice.global.common.constants.S3Constants.DIRECTORY;


@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService {

    /**
     * 추후 구현 내용
     * 1. 임시저장 이후 섹션 생성
     * 2. 섹션 삭제
     */
    private final SectionRepository sectionRepository;

    private final SectionCustomRepository sectionCustomRepository;

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    private final SurveyRepository surveyRepository;

    private final S3Config s3Uploader;

    @Transactional
    public void createSection(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<SectionDTO.SaveSectionRequestDto> sections = saveSurveyRequestDto.getSections();
        sectionCustomRepository.saveAll(sections, survey);
    }

    @Transactional
    public SectionDTO.createSectionResponseDto addSection(Long survey_id, Long member_id) {

        Survey survey = checkSurveyOwner(member_id, survey_id);

        Section section = sectionRepository.save(Section.builder().survey(survey).build());
        Question question = questionRepository.save(Question.builder().section(section).questionType(QuestionType.ONE_CHOICE).isNecessary(false).build());
        section.setQuestionOrder(String.valueOf(question.getId()));
        SectionDTO.createSectionResponseDto createSectionResponseDto = section.toResponseDto(question);

        return createSectionResponseDto;
    }

    @Transactional
    public void deleteSection(Long survey_id, Long member_id, Long section_id) {
        checkSurveyOwner(member_id, survey_id);

        if (!sectionRepository.existsById(section_id)) {
            throw new SectionNotFoundException();
        }

        sectionRepository.updateNextSectionBySectionId(section_id);
        questionOptionRepository.updateNextSectionBySectionId(section_id);
        //section 에 포함되는 img url 리스트
        List<String> optionImgList = sectionRepository.findOptionImgBySectionId(section_id);

        for (String img : optionImgList) {
            s3Uploader.delete(img, DIRECTORY);
        }
        sectionRepository.deleteById(section_id);

        /**
         * 삭제로직 추가 예정
         * 먼저 섹션에 포함되는 질문 조회해서 각 질문에 대해 질문과 옵션 조인시켜서 섹션에 포함되는 옵션 리스트를 가져오고
         * 옵션 리스트에서 img 가 null 이 아닌 list 를 가지고 와서 List<String>으로 매핑 시키고 해당 List의 값들을 S3에서 삭제
         * 이 이후에 섹션을 삭제하면 자동으로 질문이 삭제 될 것
         */
    }


    @Transactional
    public List<Section> findSectionListBySurveyId(Long surveyId) {
        return sectionRepository.findBySurveyId(surveyId);
    }


    @Transactional
    public void updateNextSection(SectionDTO.updateSectionDto updateSectionDto
            , Long surveyId, Long member_id, Long section_id) {

        checkSurveyOwner(member_id, surveyId);
        Section section = sectionRepository.findById(section_id).orElse(null);
        if (section == null) {
            throw new SectionNotFoundException();
        }
        Long nextSectionId = updateSectionDto.getNextSectionId();
        if (nextSectionId != null) {
            Section nextSection =
                    sectionRepository.findById(nextSectionId).orElse(null);
            if (nextSection == null) {
                throw new SectionNotFoundException();
            }
            section.setParentSection(nextSection);
        }
        else{
            section.setParentSection(null);
        }


    }


    /**
     * 설문 생성자가 요청한 것인지 확인
     *
     * @param member_id
     * @param survey_id
     */
    private Survey checkSurveyOwner(Long member_id, Long survey_id) {
        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if (!survey.getAuthor().getId().equals(member_id)) {
            throw new SurveyMemberMisMatchException();
        }
        return survey;
    }


}
