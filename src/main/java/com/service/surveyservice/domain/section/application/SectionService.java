package com.service.surveyservice.domain.section.application;

import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.section.dao.SectionCustomRepository;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private final SurveyRepository surveyRepository;

    @Transactional
    public void createSection(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<SectionDTO.SaveSectionRequestDto> sections = saveSurveyRequestDto.getSections();
        sectionCustomRepository.saveAll(sections, survey);
    }

    @Transactional
    public void addSection(Long survey_id){

        Section section = sectionRepository.save(Section.builder().build());
        Question question = questionRepository.save(Question.builder().section(section).build());


    }
    @Transactional
    public void updateSection(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<SectionDTO.SaveSectionRequestDto> requestSections = saveSurveyRequestDto.getSections();
        List<Section> sections = sectionRepository.findBySurveyId(survey.getId());
        Map<String, Section> sectionMap = new HashMap<>();

        for(int i=0; i<requestSections.size(); i++){
            sectionMap.put(requestSections.get(i).getId(),sections.get(i));
        }

        for(int i=0; i<requestSections.size(); i++){
            Section nextSection = sectionMap.get(requestSections.get(i).getNextSectionId());
            sections.set(i,Section.builder()
                    .id(sections.get(i).getId())
                    .survey(sections.get(i).getSurvey())
                    .parentSection(sectionMap.get(nextSection)).build());
        }
    }

    @Transactional
    public List<Section> findSectionListBySurveyId(Long surveyId){
        return sectionRepository.findBySurveyId(surveyId);
    }

    /**
     * 설문 생성자가 요청한 것인지 확인
     *
     * @param member_id
     * @param survey_id
     */
    private void checkSurveyOwner(Long member_id, Long survey_id) {
        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if (!survey.getAuthor().getId().equals(member_id)) {
            throw new SurveyMemberMisMatchException();
        }
    }


}
