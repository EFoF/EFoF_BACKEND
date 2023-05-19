package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.question.exception.exceptions.QuestionOptionNotFoundException;
import com.service.surveyservice.domain.question.model.QuestionOption;
import com.service.surveyservice.domain.survey.dao.SurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyPreMisMatchException;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import com.service.surveyservice.global.config.S3Config;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;
import static com.service.surveyservice.global.common.constants.S3Constants.DIRECTORY;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;

    private final S3Config s3Uploader;


    /**
     * 최초 설문조사 저장 시점의 저장 로직
     * @param saveSurveyRequestDto
     * @param currentMemberId
     * @return
     */
    @Transactional
    public Survey createSurvey(SaveSurveyRequestDto saveSurveyRequestDto, long currentMemberId) {
        SurveyStatus surveyStatus = SurveyStatus.PRE_RELEASE;

        // 요청으로 넘어온 사용자가 존재하는지 확인
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new NotFoundByIdException("사용자"));


        // 설문조사 저장
        return surveyRepository.save(saveSurveyRequestDto.toEntity(member, surveyStatus));

    }


    /**
     * 이미지 업로드
     * @param inputBoardImage
     * @return
     * @throws IOException
     */
    @Transactional
    public String saveSurveyImage(MultipartFile inputBoardImage) throws IOException {
        String imageUrl = s3Uploader.upload(inputBoardImage,DIRECTORY);
        return imageUrl;
    }

    /**
     * 이미지 삭제
     * @param imageUrl
     */
    @Transactional
    public void deleteSurveyImage(String imageUrl) {
        s3Uploader.delete(imageUrl,DIRECTORY);
    }

    /**
     * 설문 삭제 - S3에서 설문에 포함된 이미지 모두 삭제 -> 설문과 엮이는 데이터 삭제
     * @param member_id
     * @param survey_id
     */
    @Transactional
    public void deleteSurvey(Long member_id, Long survey_id) {
        Survey survey = checkSurveyOwner(member_id, survey_id);

        //설문 대표 사진 삭제
        String surveyImageURL = survey.getSImageURL();
        if(!surveyImageURL.isEmpty()){
            s3Uploader.delete(surveyImageURL,DIRECTORY);
        }

        //질문 항목에 사용되는 이미지 리스트 조회
        List<String> imgUrlBySurveyId = surveyRepository.findImgUrlBySurveyId(survey.getId());

        //설문에서 사용되는 모든 이미지 삭제(survey 메인 사진 제외)
        for (String img : imgUrlBySurveyId) {
            s3Uploader.delete(img,DIRECTORY);
        }

        surveyRepository.delete(survey);

    }



    /**
     * 최초 설문 작성 이후 임시저장 시 설문 대표 이미지 삭제 로직
     * @param member_id
     * @param survey_id
     * @throws IOException
     */
    @Transactional
    public void deleteSurveyImg(Long member_id,  Long survey_id) throws IOException {

        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);

        String surveyImageURL = survey.getSImageURL();
        if(!surveyImageURL.isEmpty()){
            s3Uploader.delete(surveyImageURL,DIRECTORY);
        }

        survey.setImageURL(null);
    }

    /**
     * 최초 설문 작성 이후 임시저장 시 설문 대표 이미지 수정 로직
     * @param image
     * @param member_id
     * @param survey_id
     * @throws IOException
     */
    @Transactional
    public String updateSurveyImg(MultipartFile image,
            Long member_id,  Long survey_id) throws IOException {
        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);

        String surveyImageURL = survey.getSImageURL();
        if(!(surveyImageURL==null||surveyImageURL.isEmpty())){
            s3Uploader.delete(surveyImageURL,DIRECTORY); //기존 image 삭제
        }

        String imageUrl = s3Uploader.upload(image,DIRECTORY); //새로운 image 추가

        survey.setImageURL(imageUrl);
        return imageUrl;
    }


    @Transactional
    public SurveySectionQueryDTO getSurveyDataPreRelease(Long member_id,  Long survey_id){

        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }

        //임시 저장 상태가 아닌 경우
        if(!survey.getSurveyStatus().equals(SurveyStatus.PRE_RELEASE)){
            throw new SurveyPreMisMatchException();
        }

        SurveySectionQueryDTO surveyBySurveyId = surveyRepository.findSurveyBySurveyId(survey_id);
        return surveyBySurveyId;
    }



    @Transactional
    public void updateSurveyTitle(UpdateSurveyTextDto updateSurveyTextDto,Long member_id,  Long survey_id){

        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);

        survey.setTitle(updateSurveyTextDto.getTitle());
    }
    @Transactional
    public void updateSurveyDescription(UpdateSurveyTextDto updateSurveyTextDto,Long member_id,  Long survey_id){

        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);

        survey.setDescription(updateSurveyTextDto.getDescription());
    }
    @Transactional
    public void updateSurveyColor(UpdateSurveyColorDto updateSurveyColorDto,Long member_id,  Long survey_id){

        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);
        survey.updateColor(updateSurveyColorDto);
    }

    @Transactional
    public void updateSurveyDate(UpdateSurveyDateDto updateSurveyDateDto,Long member_id,  Long survey_id){

        //설문이 존재하지 않는경우
        Survey survey = checkSurveyOwner(member_id, survey_id);
        survey.updateDate(updateSurveyDateDto);
    }

    private Survey checkSurveyOwner(Long member_id, Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id)
                .orElseThrow(SurveyNotFoundException::new);

        //설문 생성자의 요청이 아닌 경우
        if(!survey.getAuthor().getId().equals(member_id)){
            throw new SurveyMemberMisMatchException();
        }
        return survey;
    }

}
