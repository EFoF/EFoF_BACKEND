package com.service.surveyservice.domain.question.application;

import com.service.surveyservice.domain.question.dao.QuestionCustomRepository;
import com.service.surveyservice.domain.question.dao.QuestionOptionRepository;
import com.service.surveyservice.domain.question.dao.QuestionRepository;
import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.question.exception.exceptions.*;
import com.service.surveyservice.domain.question.model.Question;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.section.dao.SectionRepository;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.section.exception.exceptions.SectionNotFoundException;
import com.service.surveyservice.domain.section.exception.exceptions.SectionQuestionMissMatchException;
import com.service.surveyservice.domain.section.exception.exceptions.SurveyMissMatchException;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveySectionMisMatchException;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final S3Config s3Uploader;


    /**
     * 처음으로 설문을 생성하는 경우에 질문 생성 로직
     *
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
     *
     * @param saveQuestionRequestDto
     * @param member_id
     * @param section_id
     * @param survey_id
     * @return
     */
    @Transactional
    public QuestionDTO.ResponseSaveQuestionDto createQuestion(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
                                                              Long member_id,
                                                              Long section_id,
                                                              Long survey_id) {

        //섹션이 존재하지 않는 경우
        Section section = sectionRepository.findById(section_id)
                .orElseThrow(SectionNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if (!section.getSurvey().getAuthor().getId().equals(member_id)) {
            throw new SurveyMemberMisMatchException();
        }

        //섹션의 설문과 추가하려는 설문이 다른 경우
        if (!section.getSurvey().getId().equals(survey_id)) {
            throw new SurveyMissMatchException();
        }

        Question question = saveQuestionRequestDto.toEntity(section);
        String questionOrder = section.getQuestionOrder();

        Question savedQuestion = questionRepository.save(question);

        //section에 question order 수정 로직
        if (questionOrder == null || questionOrder.isBlank()) {//글자가 없는 경우 question이 0개인 경우
            section.setQuestionOrder(String.valueOf(savedQuestion.getId()));
        } else if (questionOrder.length() == 1) { //1글자인 경우 즉 question이 1개인 경우 -> 1 이므로 split 이 안댐
            section.setQuestionOrder(questionOrder.concat(",").concat(String.valueOf(savedQuestion.getId())));
        } else {
            section.setQuestionOrder(questionOrder.concat("," + savedQuestion.getId()));
        }

        return savedQuestion.toResponseDto();
    }


    /**
     * 이미 설문 및 질문이 생성되어 있는 경우 설문에 질문을 수정(이미지와 섹션은 따로)하는 로직
     *
     * @param saveQuestionRequestDto
     * @param member_id
     * @param question_id
     * @param survey_id
     */
    @Transactional
    public void updateQuestionContent(QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
                                      Long member_id,
                                      Long question_id,
                                      Long survey_id,
                                      Long section_id) {

        checkSurveyOwner(member_id, survey_id);

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        if (!question.getSection().getId().equals(section_id)) {
            throw new SectionQuestionMissMatchException();
        }
        question.updateQuestion(saveQuestionRequestDto);
    }


    /**
     * 질문 삭제하는 로직 - cascade.remove 로 연관된 데이터 다 삭제되고 S3의 이미지도 삭제된다.
     *
     * @param question_id
     * @param survey_id
     * @param member_id
     */
    @Transactional
    public void deleteQuestion(Long question_id, Long survey_id, Long member_id) {

        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        //해당 질문에 포함된 image 리스트
        List<String> imgUrlList = questionRepository.findImgUrlByQuestionId(question.getId());

        for (String img : imgUrlList) {
            s3Uploader.delete(img, DIRECTORY);
        }

        Section section = question.getSection();

        String questionOrder = section.getQuestionOrder();

        //section에 question order 수정 로직
        if (questionOrder.length() == 1) { //1글자인 경우 즉 question 이 1개인 경우 모든 question 이 없어지는 것
            section.setQuestionOrder(null);
        } else {
            List<String> questionOrderList = new ArrayList<>(Arrays.asList(questionOrder.split(",")));
            questionOrderList.remove(question.getId().toString());

            section.setQuestionOrder(
                    String.join(",",
                            questionOrderList));

        }

        //cascade 를 통해 자동으로 questionOption 도 삭제된다.
        questionRepository.delete(question);
    }


    /**
     * 생성되어 있는 질문에 questionOption 이 추가되는 경우 - img와 같은 정보는 안들어 있을 것임.
     *
     * @param saveQuestionOptionTextRequestDTO
     * @param member_id
     * @param question_id
     * @param survey_id
     */
    @Transactional
    public Long createQuestionOption(QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
                                     Long member_id, Long question_id, Long survey_id) {

        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        //질문이 존재하지 않는 경우
        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        QuestionOption questionOption = saveQuestionOptionTextRequestDTO.toEntity(question);

        QuestionOption savedQuestionOption = questionOptionRepository.save(questionOption);
        return savedQuestionOption.getId();
    }

    /**
     * 질문 항목의 내용 변경
     *
     * @param saveQuestionOptionTextRequestDTO
     * @param member_id
     * @param question_option_id
     * @param survey_id
     */
    @Transactional
    public void updateQuestionOptionText(QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
                                         Long member_id, Long question_option_id, Long survey_id) {

        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        questionOption.setQuestionOptionText(saveQuestionOptionTextRequestDTO.getOptionText());
    }

    /**
     * 질문 항목이 가르키는 다음 섹션 변경
     *
     * @param saveQuestionOptionNextSectionRequestDTO
     * @param member_id
     * @param question_option_id
     * @param survey_id
     */
    @Transactional
    public void updateQuestionOptionNextSection(QuestionOptionDTO.SaveQuestionOptionNextSectionRequestDTO saveQuestionOptionNextSectionRequestDTO,
                                                Long member_id, Long question_option_id, Long survey_id) {

        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);
        //다음 섹션 변경이면 옵션은 존재해야하는데 없는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        Section nextSection = sectionRepository.findById(saveQuestionOptionNextSectionRequestDTO.getNextSection_id())
                .orElseThrow(SectionNotFoundException::new);

        questionOption.setQuestionOptionNextSection(nextSection);
    }

    /**
     * 질문 항목의 이미지 업데이트 - 기존 S3 이미지 삭제 후 새로운 이미지 추가 및 세팅
     *
     * @param image
     * @param member_id
     * @param question_option_id
     * @param survey_id
     * @return
     * @throws IOException
     */
    @Transactional
    public String updateQuestionOptionImg(MultipartFile image,
                                          Long member_id, Long question_option_id, Long survey_id) throws IOException {


        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        //이미지 전송 -> 저장 -> return 받은 url 로 업데이트
        String imageUrl = s3Uploader.upload(image, DIRECTORY);

        String questionOptionImg = questionOption.getQuestionOptionImg();

        if (!(questionOptionImg == null || questionOptionImg.isEmpty())) {//해당 option 에 대한 optionImg 가 생성되어 있는 경우
            s3Uploader.delete(questionOptionImg, DIRECTORY);
        }
        questionOption.setQuestionOptionImage(imageUrl);
        return imageUrl;
    }


    /**
     * 질문 항목의 이미지 삭제 - S3 데이터 삭제 및 QuestionOptionImage 테이블 데이터 삭제
     *
     * @param member_id
     * @param question_option_id
     * @param survey_id
     * @throws IOException
     */
    @Transactional
    public void deleteQuestionOptionImg(Long member_id, Long question_option_id, Long survey_id) throws IOException {

        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        String questionOptionImg = questionOption.getQuestionOptionImg();

        if (!(questionOptionImg == null || questionOptionImg.isEmpty())) {//해당 option 에 대한 optionImg 가 생성되어 있는 경우
            s3Uploader.delete(questionOptionImg, DIRECTORY);
        }

        questionOption.setQuestionOptionImage(null);
    }


    /**
     * 질문 항목 삭제 - S3의 이미지 삭제 후 QuestionOption 삭제 -> QuestionOptionImage 도 삭제된다.
     *
     * @param member_id
     * @param question_option_id
     * @param survey_id
     * @throws IOException
     */
    @Transactional
    public void deleteQuestionOption(Long member_id, Long question_option_id, Long survey_id) throws IOException {


        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        //questionOption이 존재하지 않는 경우
        QuestionOption questionOption = questionOptionRepository.findById(question_option_id)
                .orElseThrow(QuestionOptionNotFoundException::new);

        String img = questionOption.getQuestionOptionImg();
        if (!(img == null || img.isEmpty())) {
            s3Uploader.delete(img, DIRECTORY);
        }

        questionOptionRepository.delete(questionOption);
    }


    /**
     * question 의 section 변경 및 section 의 question order 변경
     *
     * @param survey_id
     * @param member_id
     * @param question_id
     */
    @Transactional
    public SectionDTO.UpdateSectionOrderResponseDto updateQuestionOrder(Long survey_id, Long member_id,
                                                                        SectionDTO.UpdateSectionOrderRequestDto updateSectionOrderRequestDto,
                                                                        Long question_id) {
        //설문이 존재하지 않는경우
        checkSurveyOwner(member_id, survey_id);

        Question question = questionRepository.findById(question_id)
                .orElseThrow(QuestionNotFoundException::new);

        Section startSection = sectionRepository.findById(updateSectionOrderRequestDto.getStartSectionId())
                .orElseThrow(SectionNotFoundException::new);


        Section endSection = sectionRepository.findById(updateSectionOrderRequestDto.getEndSectionId())
                .orElseThrow(SectionNotFoundException::new);

        if (!(startSection.getSurvey().getId().equals(survey_id) && endSection.getSurvey().getId().equals(survey_id))) {
            throw new SurveySectionMisMatchException();
        }

        //시작 섹션이 원래 question의 section id 이므로 검사
        if (!question.getSection().getId().equals(updateSectionOrderRequestDto.getStartSectionId())) {
            throw new QuestionSectionMisMatchException();
        }


        String startQuestionOrder = startSection.getQuestionOrder();

        if (!startQuestionOrder.contains(",")) {//1글자인 경우 즉 question이 1개인 경우 -> 1 이므로 그냥 지우면 된다.
            startSection.setQuestionOrder(null);
        } else if (startQuestionOrder.isEmpty()) {//비어있는 경우 삭제할 수가 없으므로 예외
            throw new QuestionOrderException();
        } else {//question이 여러개인 경우
            List<String> order = new ArrayList<>(Arrays.asList(startQuestionOrder.split(",")));

            if (!order.get(updateSectionOrderRequestDto.getStartSectionIdx())
                    .equals(question_id.toString())) {
                throw new SectionQuestionMissMatchException();
            }

            order.remove(updateSectionOrderRequestDto.getStartSectionIdx());

            if (order.size() != 0) {
                startSection.setQuestionOrder(new StringBuilder(order.get(0))
                        .append(order.stream().skip(1).map(s -> "," + s).collect(Collectors.joining())).toString());
            }
        }
        String endQuestionOrder;

        if (startSection.getId().equals(endSection.getId())) {
            endQuestionOrder =
                    Optional.ofNullable(startSection.getQuestionOrder()).orElse(null);
            if (endQuestionOrder == null) {
                throw new QuestionOrderException();
            }

        } else {
            endQuestionOrder = Optional.ofNullable(endSection.getQuestionOrder()).orElse(null);
        }


        if ( endQuestionOrder == null||endQuestionOrder.isEmpty()) {//글자가 없는 경우 question이 0개인 경우
            endSection.setQuestionOrder(String.valueOf(question.getId()));
        } else if (!endQuestionOrder.contains(",")) { //1글자인 경우 즉 question이 1개인 경우 -> 1 이므로 split 이 안댐
            if (updateSectionOrderRequestDto.getEndSectionIdx() == 0) {
                endSection.setQuestionOrder(String.valueOf(question_id).concat(",").concat(endQuestionOrder));
            } else {
                endSection.setQuestionOrder(endQuestionOrder.concat(",").concat(String.valueOf(question_id)));
            }

        } else {//question이 여러개인 경우 1,2,3 이런식으로 되어있음
            List<String> order = new ArrayList<>(Arrays.asList(endQuestionOrder.split(",")));
            order.add(updateSectionOrderRequestDto.getEndSectionIdx(), String.valueOf(question_id));
            endSection.setQuestionOrder(new StringBuilder(order.get(0))
                    .append(order.stream().skip(1).map(s -> "," + s).collect(Collectors.joining())).toString());
        }

        question.updateSection(endSection);

        return SectionDTO.UpdateSectionOrderResponseDto.builder()
                .questionSectionId(endSection.getId())
                .startSectionOrder(startSection.getQuestionOrder())
                .endSectionOrder(endSection.getQuestionOrder()).build();
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
