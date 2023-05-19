package com.service.surveyservice.domain.section.api;

import com.service.surveyservice.domain.section.application.SectionService;
import com.service.surveyservice.domain.section.dto.SectionDTO;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    /**
     * 구현해야 할 기능들 - 임시저장에 사용될 것들임
     * 1. 섹션 삭제 - 섹션에 엮인 질문, 질문 항목 , 항목의 이미지, S3 의 이미지 포함
     * 2. 섹션 생성
     */



    @PostMapping(value = "/survey/{survey_id}/section")
    public ResponseEntity<SectionDTO.createSectionResponseDto> addSection(
            @PathVariable Long survey_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        SectionDTO.createSectionResponseDto createSectionResponseDto = sectionService.addSection(survey_id,currentMemberId);
        return new ResponseEntity<>(createSectionResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/survey/{survey_id}/section/{section_id}")
    public ResponseEntity deleteSection(
            @PathVariable Long survey_id, @PathVariable Long section_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        sectionService.deleteSection(survey_id,currentMemberId,section_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/survey/{survey_id}/section/{section_id}")
    public ResponseEntity updateSection(
            @RequestBody SectionDTO.updateSectionDto updateSectionDto,
            @PathVariable Long survey_id, @PathVariable Long section_id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        sectionService.updateNextSection(updateSectionDto,survey_id,currentMemberId,section_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
