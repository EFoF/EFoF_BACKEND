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
@RequestMapping("/form")
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

//    @GetMapping(value = "/form/participate/{memberId}")
//    public ResponseEntity<Page<SurveyInfoDTO>> getParticipatedSurveyList(@PathVariable(name = "memberId") Long memberId, Pageable pageable) {
//        Long currentMemberId = SecurityUtil.getCurrentMemberId();
//        Page<MemberSurveyInfoDTO> infos = memberSurveyService.getInfoPagination(currentMemberId, memberId, pageable);
//        Page<SurveyInfoDTO> participatedSurveyInfo = surveyService.getParticipatedSurveyInfo(infos);
//        return new ResponseEntity<>(participatedSurveyInfo, HttpStatus.OK);
//    }

    /**
     *
     * @param image
     * @return 설문조사 대표 이미지 생성
     * @throws IOException
     */
    @PostMapping(value = "/image")
    public String saveImage(@RequestBody MultipartFile image) throws IOException {

        return surveyService.saveSurveyImage(image);
    }

    /**
     * 설문조사 대표 이미지 삭제
     * @param imageUrl
     */
    @DeleteMapping(value = "/image")
    public void deleteImage(@RequestBody String imageUrl) {
        surveyService.deleteSurveyImage(imageUrl);
    }


    /**
     * 추후 구현 계획
     * <p>
     * 1. 임시 저장(수정)을 위한 survey 테이블 관련 데이터 변경
     * 2. 설문조사 대표 이미지 변경 - S3 데이터 삭제 및 추가 후 S3 url로 db column update
     * 3. 날짜 및 제약 사항 추가(배포 페이지에서 하는 것)
     * 4. 설문조사 삭제 - 설문 관련 정보 모두 삭제
     * 5. 설문조사 내용 조회 - 설문조사 참여 페이지에 띄워줄 데이터 조회
     * 6. 설문조사 리스트 조회 - survey 테이블의 데이터 조회정도(어떤 설문이 있는지의 리스트를 보여주기 위함이므로 질문과 같은 데이터는 필요 없음)
     *
     * @return
     */


    @GetMapping(value = "/{survey_id}/pre_release")
    public ResponseEntity<SurveySectionQueryDTO> getSurveyDataPreRelease(@PathVariable Long survey_id){
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return new ResponseEntity<>(
                surveyService.getSurveyDataPreRelease(currentMemberId,survey_id),
                HttpStatus.CREATED);
    }
}