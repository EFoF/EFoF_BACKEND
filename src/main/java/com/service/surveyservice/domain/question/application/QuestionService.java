package com.service.surveyservice.domain.question.application;

import com.service.surveyservice.domain.question.dao.QuestionCustomRepository;
import com.service.surveyservice.domain.question.dao.QuestionOptionImgRepository;
import com.service.surveyservice.domain.question.dao.QuestionOptionRepository;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.question.exception.exceptions.QuestionNotFoundException;
import com.service.surveyservice.domain.question.exception.exceptions.QuestionOptionNotFoundException;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.question.model.QuestionOptionImg;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.exception.exceptions.SectionNotFoundException;
import com.service.surveyservice.domain.section.exception.exceptions.SurveyMissMatchException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.service.surveyservice.global.common.constants.S3Constants.DIRECTORY;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final SurveyRepository surveyRepository;
    private final SectionRepository sectionRepository;
    private final QuestionCustomRepository questionCustomRepository;

    private final QuestionRepository questionRepository;

    private final QuestionOptionRepository questionOptionRepository;

    private final QuestionOptionImgRepository questionOptionImgRepository;
    private final S3Config s3Uploader;



    /**
     * 처음으로 설문을 생성하는 경우에 질문 생성 로직
     * @param saveSurveyRequestDto
     * @param survey
     */
    @Transactional
    public void createQuestionInit(SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto, Survey survey) {

        List<Section> sectionList = sectionRepository.findBySurveyId(survey.getId());
        questionCustomRepository.saveAll(sectionList, saveSurveyRequestDto);
    }


    /**
     * 이미 설문 및 질문이 생성되어 있는 경우 설문에 질문을 추가하는 로직
     * @param saveQuestionRequestDto
     * @param member_id
     * @param section_id
     * @param survey_id
     */
    @Transactional
    public void createQuestion(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
                               Long member_id,
                               Long section_id,
                               Long survey_id) {

        //섹션이 존재하지 않는 경우
        Section section = sectionRepository.findById(section_id)
                .orElseThrow(SectionNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!section.getSurvey().getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //섹션의 설문과 추가하려는 설문이 다른 경우
        if(!section.getSurvey().getId().equals(survey_id)){
            throw new SurveyMissMatchException();
        }

        Question question = saveQuestionRequestDto.toEntity(section);

        questionRepository.save(question);
    }


    /**
     * 이미 설문 및 질문이 생성되어 있는 경우 설문에 질문을 수정하는 로직
     * @param saveQuestionRequestDto
     * @param member_id
     * @param question_id
     * @param survey_id
     */
    @Transactional
    public void updateQuestionContent(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
                               Long member_id,
                               Long question_id,
                               Long survey_id) {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        question.updateQuestion(saveQuestionRequestDto);
    }

    @Transactional
    public void deleteQuestion(Long question_id,Long survey_id,Long member_id) {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        List<String> imgUrlList = questionRepository.findImgUrlByQuestionId(question.getId());

        for (String img : imgUrlList) {
            s3Uploader.delete(img,DIRECTORY);
        }

        //cascade 를 통해 자동으로 questionOption , questionOptionImg 도 삭제된다.
        questionRepository.delete(question);
    }


    /**
     * 생성되어 있는 질문에 questionOption 이 추가되는 경우 - img와 같은 정보는 안들어 있을 것임.
     * @param saveQuestionOptionTextRequestDTO
     * @param member_id
     * @param question_id
     * @param survey_id
     */
    @Transactional
    public void createQuestionOption(QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
                                     Long member_id, Long question_id, Long survey_id) {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        QuestionOption questionOption = saveQuestionOptionTextRequestDTO.toEntity(question);

        questionOptionRepository.save(questionOption);
    }

    @Transactional
    public void updateQuestionOptionText(QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
                                         Long member_id, Long question_option_id, Long survey_id) {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        questionOption.setQuestionOptionText(saveQuestionOptionTextRequestDTO.getOptionText());
    }

    @Transactional
    public void updateQuestionOptionNextSection(QuestionOptionDTO.SaveQuestionOptionNextSectionRequestDTO saveQuestionOptionNextSectionRequestDTO,
                                                Long member_id, Long question_option_id, Long survey_id) {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        Section nextSection = sectionRepository.findById(saveQuestionOptionNextSectionRequestDTO.getNextSection_id())
                .orElseThrow(SectionNotFoundException::new);

        questionOption.setQuestionOptionNextSection(nextSection);
    }

    @Transactional
    public void updateQuestionOptionImg(MultipartFile image,
                                        Long member_id, Long question_option_id, Long survey_id) throws IOException {


        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        //이미지 전송 -> 저장 -> return 받은 url 로 업데이트
        String imageUrl = s3Uploader.upload(image,DIRECTORY);
        QuestionOptionImg questionOptionImg = questionOption.getQuestionOptionImg();

        if(questionOptionImg==null){//해당 option 에 대한 optionImg 가 생성되어 있지 않은 경우

            QuestionOptionImg optionImg = QuestionOptionImg.builder()
                    .imgUrl(imageUrl).build();
            questionOptionImgRepository.save(optionImg);
        }
        else{ //이미 다른 optionImg 가 생성되어 있는 경우 이름만 바꿔줌
            questionOptionImg.setImgUrl(imageUrl);
        }
    }

    @Transactional
    public void deleteQuestionOption(Long member_id, Long question_option_id, Long survey_id) throws IOException {


        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        String img = questionOption.getQuestionOptionImg().getImgUrl();
        s3Uploader.delete(img,DIRECTORY);
        questionOptionRepository.delete(questionOption);
    }

    @Transactional
    public void deleteQuestionOptionImg(Long member_id, Long question_option_id, Long survey_id) throws IOException {

        //설문이 존재하지 않는경우
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        String img = questionOption.getQuestionOptionImg().getImgUrl();
        s3Uploader.delete(img,DIRECTORY);

        questionOption.setQuestionOptionImg(null);
    }

}
