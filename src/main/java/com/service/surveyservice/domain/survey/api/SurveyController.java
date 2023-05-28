package com.service.surveyservice.domain.survey.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.question.application.QuestionService;
import com.service.surveyservice.domain.section.application.SectionService;
import com.service.surveyservice.domain.survey.application.MemberSurveyService;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

@Slf4j
@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final MemberService memberService;
    private final SurveyService surveyService;
    private final MemberSurveyService memberSurveyService;

    private final SectionService sectionService;

    private final QuestionService questionService;

    /**
     * 설문조사 생성
     * @param saveSurveyRequestDto
     * @return
     */

    @PostMapping
    public ResponseEntity<Long> createSurvey(@RequestBody SaveSurveyRequestDto saveSurveyRequestDto) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Survey survey = surveyService.createSurvey(saveSurveyRequestDto, currentMemberId);
        sectionService.createSection(saveSurveyRequestDto, survey);
        questionService.createQuestionInit(saveSurveyRequestDto, survey);

        return new ResponseEntity<>(survey.getId(),HttpStatus.CREATED);
    }

    /**
     *
     * @param image
     * @return 임시저장 전 -  설문조사 대표 이미지 생성
     * @throws IOException
     */
    @PostMapping(value = "/image")
    public String saveImage(@RequestBody MultipartFile image) throws IOException {

        return surveyService.saveSurveyImage(image);
    }

    /**
     * 임시저장 전 - 설문조사 대표 이미지 삭제
     * @param imageUrl
     */
    @DeleteMapping(value = "/image")
    public void deleteImage(@RequestParam String imageUrl) {
        surveyService.deleteSurveyImage(imageUrl);
    }


    /**
     * 임시 저장 이후 설문 삭제
     * @param survey_id
     * @return
     * @throws IOException
     */
    @DeleteMapping(value = "/{survey_id}")
    public ResponseEntity deleteSurvey(@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.deleteSurvey(currentMemberId,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    /**
     * 임시저장 이후 제목 변경
     * @param updateSurveyTextDto
     * @param survey_id
     * @return
     * @throws IOException
     */
    @PatchMapping(value = "/{survey_id}/title")
    public ResponseEntity updateSurveyTitle(@RequestBody UpdateSurveyTextDto updateSurveyTextDto,@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyTitle(updateSurveyTextDto,currentMemberId, survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 임시저장 이후 설명 변경
     * @param updateSurveyTextDto
     * @param survey_id
     * @return
     * @throws IOException
     */
    @PatchMapping(value = "/{survey_id}/description")
    public ResponseEntity updateSurveyDescription(@RequestBody UpdateSurveyTextDto updateSurveyTextDto,@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyDescription(updateSurveyTextDto, currentMemberId, survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 임시저장 이후 색상 변경
     * @param updateSurveyColorDto
     * @param survey_id
     * @return
     * @throws IOException
     */
    @PatchMapping(value = "/{survey_id}/color")
    public ResponseEntity updateSurveyColor(@RequestBody UpdateSurveyColorDto updateSurveyColorDto,@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyColor(updateSurveyColorDto, currentMemberId, survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 임시저장 이후 이미지 변경
     * @param image
     * @param survey_id
     * @return
     * @throws IOException
     */
    @PatchMapping(value = "/{survey_id}/image")
    public ResponseEntity<String> updateSurveyImg(@RequestBody MultipartFile image,@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        String surveyImg = surveyService.updateSurveyImg(image, currentMemberId, survey_id);
        return new ResponseEntity<>(surveyImg,HttpStatus.OK);
    }

    /**
     * 임시저장 이후 이미지 삭제
     * @param survey_id
     * @return
     * @throws IOException
     */
    @DeleteMapping(value = "/{survey_id}/image")
    public ResponseEntity deleteSurveyImg(@PathVariable Long survey_id) throws IOException {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.deleteSurveyImg(currentMemberId,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 임시 저장 중인 데이터 조회
     * @param survey_id
     * @return
     */
    @GetMapping(value = "/{survey_id}/pre_release")
    public ResponseEntity<SurveySectionQueryDTO> getSurveyDataPreRelease(@PathVariable Long survey_id){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return new ResponseEntity<>(surveyService.getSurveyDataPreRelease(currentMemberId,survey_id),HttpStatus.OK);
    }



    @PostMapping(value = "/{survey_id}/setting/open_date")
    public ResponseEntity updateSurveyOpenDate(@PathVariable Long survey_id,@RequestBody UpdateSurveyDateDto updateSurveyDateDto){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyOpenDate(updateSurveyDateDto,currentMemberId,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{survey_id}/setting/expire_date")
    public ResponseEntity updateSurveyExpireDate(@PathVariable Long survey_id,@RequestBody UpdateSurveyDateDto updateSurveyDateDto){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyExpireDate(updateSurveyDateDto,currentMemberId,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{survey_id}/setting/stat")
    public ResponseEntity updateSurveyStat(@PathVariable Long survey_id,@RequestBody UpdateSurveySettingDto updateSurveySettingDto){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        surveyService.updateSurveyStat(updateSurveySettingDto,currentMemberId,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 임시 저장 중인 데이터 조회
     * @param survey_id
     * @return
     */
    @GetMapping(value = "/{survey_id}/setting")
    public ResponseEntity<SurveySectionQueryDTO> getSurveyDataSetting(@PathVariable Long survey_id){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return new ResponseEntity<>(surveyService.getSurveyDataSetting(currentMemberId,survey_id),HttpStatus.OK);
    }


}