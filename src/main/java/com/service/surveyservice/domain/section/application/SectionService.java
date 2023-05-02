package com.service.surveyservice.domain.section.application;

import com.service.surveyservice.domain.section.dao.SectionCustomRepository;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
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

    private final SectionRepository sectionRepository;

    private final SectionCustomRepository sectionCustomRepository;
    @Transactional
    public void createSection(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<SectionDTO.SaveSectionRequestDto> sections = saveSurveyRequestDto.getSections();
        sectionCustomRepository.saveAll(sections, survey);
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
}
