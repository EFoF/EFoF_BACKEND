package com.service.surveyservice.domain.question.api;

import com.service.surveyservice.domain.question.application.QuestionService;
import com.service.surveyservice.domain.question.dto.QuestionDTO;
import com.service.surveyservice.domain.question.dto.QuestionOptionDTO;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class QuestionController {
    /**
     * 구현해야 할 기능들 - 임시저장에 사용될 것들임
     * 1. 질문 생성
     * 2. 질문 삭제 - 질문 항목 , 항목의 이미지, S3 의 이미지 포함
     * 3. 항목 생성
     * 4. 항목의 이미지 생성 - S3의 이미지 포함
     * 5. 질문 수정(text 수정, 필수 항목 여부 수정, type 수정-객관식->주관식 수정 시 기존 객관식 데이터 삭제해야함 and 찬부식의 경우에도 마찬가지 )
     */

    private final QuestionService questionService;



    @PostMapping(value = "/{survey_id}/section/{section_id}/question")
    public ResponseEntity<QuestionDTO.ResponseSaveQuestionDto> createQuestion(
            @RequestBody QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
            @PathVariable Long section_id, @PathVariable Long survey_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        QuestionDTO.ResponseSaveQuestionDto question = questionService.createQuestion(saveQuestionRequestDto, currentMemberId, section_id, survey_id);
        return new ResponseEntity<>(question,HttpStatus.CREATED);
    }

    /**
     * 질문생성 - 테스트 완료
     */
    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}")
    public ResponseEntity updateQuestionContent(
            @RequestBody QuestionDTO.SaveQuestionRequestDto saveQuestionRequestDto,
            @PathVariable Long section_id, @PathVariable Long survey_id, @PathVariable Long question_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.updateQuestionContent(saveQuestionRequestDto, currentMemberId, question_id, survey_id,section_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}")
    public ResponseEntity deleteQuestion(
            @PathVariable Long section_id, @PathVariable Long survey_id, @PathVariable Long question_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.deleteQuestion(question_id, survey_id, currentMemberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}")
    public ResponseEntity<Long> createQuestionOption(
            @RequestBody QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        Long questionOptionId = questionService.createQuestionOption(saveQuestionOptionTextRequestDTO, currentMemberId, question_id, survey_id);
        return new ResponseEntity<>(questionOptionId,HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/question_option/{question_option_id}")
    public ResponseEntity updateQuestionOptionText(
            @RequestBody QuestionOptionDTO.SaveQuestionOptionTextRequestDTO saveQuestionOptionTextRequestDTO,
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id,@PathVariable Long question_option_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.updateQuestionOptionText(saveQuestionOptionTextRequestDTO,currentMemberId,question_option_id,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/question_option/{question_option_id}/section")
    public ResponseEntity updateQuestionOptionNextSection(
            @RequestBody QuestionOptionDTO.SaveQuestionOptionNextSectionRequestDTO saveQuestionOptionNextSectionRequestDTO,
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id,@PathVariable Long question_option_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.updateQuestionOptionNextSection(saveQuestionOptionNextSectionRequestDTO,currentMemberId,question_option_id,survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/question_option/{question_option_id}/image")
    public ResponseEntity<String> updateQuestionOptionImg(
            @RequestBody MultipartFile image,
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id,@PathVariable Long question_option_id) throws IOException {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        String img = questionService.updateQuestionOptionImg(image, currentMemberId, question_option_id, survey_id);
        return new ResponseEntity<>(img,HttpStatus.OK);
    }

    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/question_option/{question_option_id}/image/delete")
    public ResponseEntity deleteQuestionOptionImg(
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id,@PathVariable Long question_option_id) throws IOException {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.deleteQuestionOptionImg(currentMemberId, question_option_id, survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/question_option/{question_option_id}")
    public ResponseEntity deleteQuestionOption(
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id,@PathVariable Long question_option_id) throws IOException {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        questionService.deleteQuestionOption(currentMemberId, question_option_id, survey_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping(value = "/{survey_id}/section/{section_id}/question/{question_id}/order")
    public ResponseEntity<SectionDTO.UpdateSectionOrderResponseDto> updateQuestionOrder(
            @RequestBody SectionDTO.UpdateSectionOrderRequestDto updateSectionOrderRequestDto,
            @PathVariable Long question_id, @PathVariable Long section_id, @PathVariable Long survey_id) throws IOException {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        SectionDTO.UpdateSectionOrderResponseDto updateSectionOrderResponseDto = questionService.updateQuestionOrder(survey_id, currentMemberId, updateSectionOrderRequestDto, question_id);
        return new ResponseEntity<>(updateSectionOrderResponseDto,HttpStatus.OK);
    }
}