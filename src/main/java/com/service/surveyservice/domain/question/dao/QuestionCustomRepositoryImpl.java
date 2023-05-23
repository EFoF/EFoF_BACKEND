package com.service.surveyservice.domain.question.dao;

import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final QuestionRepository questionRepository;


    private final QuestionOptionRepository questionOptionRepository;

    @Override
    public void saveAll(List<Section> sectionList, SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto) {

        Map<String, Section> sectionMap = new HashMap<>();

        List<SectionDTO.SaveSectionRequestDto> requestSections = saveSurveyRequestDto.getSections();

        for (int i = 0; i < requestSections.size(); i++) {
            sectionMap.put(requestSections.get(i).getId(), sectionList.get(i));
        }

        for (int j = 0; j < sectionList.size(); j++) {
            StringBuilder sb = new StringBuilder(); //questionOrder 를 만들기 위한 StringBuilder
            Section section = sectionList.get(j);
            Section nextSection = sectionMap.get(requestSections.get(j).getNextSectionId());

            List<QuestionDTO.SaveSurveyQuestionRequestDto> questionList = saveSurveyRequestDto.getSections().get(j).getQuestionList();


            List<Question> questions    //request 에서 받아온 question 들을 각각의 section 마다 저장 후 question 리스트 반환
                    = questionRepository.saveAll(QuestionDTO.toEntities(questionList, section));

            for(int k=0; k<questions.size(); k++){
                sb.append(questions.get(k).getId()).append(",");

                List<QuestionOptionDTO.SaveQuestionOptionRequestDTOInit> options = questionList.get(k).getOptions();
                List<QuestionOption> questionOptionList = new ArrayList<>();
                for (int i=0; i< options.size(); i++){

                    Section optionNextSection = sectionMap.get(requestSections.get(j).getQuestionList().get(k).getOptions().get(i).getNextSectionId());
                    QuestionOption questionOption = options.get(i).toQuestionOptionEntity(optionNextSection,questions.get(k));

                    questionOptionList.add(questionOption);
                }

                questionOptionRepository.saveAll(questionOptionList);
            }

            sb.deleteCharAt(sb.length() - 1);

            section.setParentSection(nextSection);
            section.setQuestionOrder(String.valueOf(sb));

        }
    }
}
