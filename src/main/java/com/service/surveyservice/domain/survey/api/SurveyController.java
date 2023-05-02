package com.service.surveyservice.domain.survey.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.question.application.QuestionService;
import com.service.surveyservice.domain.section.application.SectionService;
import com.service.surveyservice.domain.section.model.Section;
import com.service.surveyservice.domain.survey.application.MemberSurveyService;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.model.MemberSurvey;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;
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
    @PostMapping
    public ResponseEntity<SurveyInfoDTO> createSurvey(@RequestBody SaveSurveyRequestDto saveSurveyRequestDto) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Survey survey = surveyService.createSurvey(saveSurveyRequestDto, currentMemberId);
        sectionService.createSection(saveSurveyRequestDto,survey);
//        List<Section> sectionList = sectionService.findSectionListBySurveyId(survey.getId());
        questionService.createQuestion(saveSurveyRequestDto,survey);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/form/participate/{memberId}")
    public ResponseEntity<Page<SurveyInfoDTO>> getParticipatedSurveyList(@PathVariable(name = "memberId") Long memberId, Pageable pageable) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Page<MemberSurveyInfoDTO> infos = memberSurveyService.getInfoPagination(currentMemberId, memberId, pageable);
        Page<SurveyInfoDTO> participatedSurveyInfo = surveyService.getParticipatedSurveyInfo(infos);
        return new ResponseEntity<>(participatedSurveyInfo, HttpStatus.OK);
    }

    @PostMapping(value = "/image")
    public String saveImage(@RequestBody MultipartFile image) throws IOException {

        return surveyService.saveSurveyImage(image);
    }

    @DeleteMapping(value = "/image")
    public void deleteBoardImage(@RequestBody String imageUrl) {
        surveyService.deleteSurveyImage(imageUrl);
    }

//    @PostMapping
//    public SaveSurveyRequestDto saveSurvey(@RequestBody SaveSurveyRequestDto saveSurveyRequestDto) throws IOException {
//            log.info(saveSurveyRequestDto.toString());
//            return saveSurveyRequestDto;
//    }



}
