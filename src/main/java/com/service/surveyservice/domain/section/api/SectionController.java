package com.service.surveyservice.domain.section.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
public class SectionController {
    /**
     * 구현해야 할 기능들 - 임시저장에 사용될 것들임
     * 1. 섹션 삭제 - 섹션에 엮인 질문, 질문 항목 , 항목의 이미지, S3 의 이미지 포함
     * 2. 섹션 생성
     */
}
