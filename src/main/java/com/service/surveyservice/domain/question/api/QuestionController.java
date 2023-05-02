package com.service.surveyservice.domain.question.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/question")
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
}