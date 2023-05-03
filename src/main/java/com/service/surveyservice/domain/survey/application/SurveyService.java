package com.service.surveyservice.domain.survey.application;

import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.exception.exceptions.member.NotMatchingCurrentMemberAndRequesterException;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.dao.SurveyCustomRepositoryImpl;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.MemberSurveyDTO;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.exception.ExpireBeforeOpenException;
import com.service.surveyservice.domain.survey.exception.SurveyConvertException;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.domain.survey.model.SurveyStatus;
import com.service.surveyservice.global.config.S3Config;
import com.service.surveyservice.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.service.surveyservice.domain.survey.dto.MemberSurveyDTO.*;
import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;
import static com.service.surveyservice.global.common.constants.S3Constants.DIRECTORY;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyCustomRepositoryImpl surveyCustomRepository;


    private final S3Config s3Uploader;

    @Transactional
    public Survey createSurvey(SaveSurveyRequestDto saveSurveyRequestDto, long currentMemberId) {
        SurveyStatus surveyStatus = SurveyStatus.PRE_RELEASE;

        // 요청으로 넘어온 사용자가 존재하는지 확인
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new NotFoundByIdException("사용자"));


        // 설문조사 저장
        return surveyRepository.save(saveSurveyRequestDto.toEntity(member, surveyStatus));

    }

    public Page<SurveyInfoDTO> getAuthorSurveyList(Long memberId, Long currentMemberId, Pageable pageable) {
        // 아이디 검증
        if(!memberId.equals(currentMemberId)) {
            throw new NotMatchingCurrentMemberAndRequesterException();
        }
        Page<SurveyInfoDTO> surveyInfoDTOPage = surveyCustomRepository.findSurveyInfoDTOByAuthorId(memberId, pageable);
        return surveyInfoDTOPage;
    }

    // MemberSurveyService에서 받아온 MemberSurveyInfoDTO의 page를 api에서 사용하기 위해 SurveyInfoDTO로 변환 시켜주는 작업
    public Page<SurveyInfoDTO> getParticipatedSurveyInfo(Page<MemberSurveyInfoDTO> infoPage) {
        Page<SurveyInfoDTO> surveyInfoDTOPage = infoPage.map(element -> {
            Long id = element.getId();
            return _findSurveyInfoById(id);
        });
        return surveyInfoDTOPage;
    }

    // 내부적으로 사용되는 메서드. id로 SurveyInfoDTO를 찾아온다.
    private SurveyInfoDTO _findSurveyInfoById (Long surveyId) {
        SurveyInfoDTO surveyInfoDTO = surveyRepository.findSurveyInfoDTOById(surveyId).orElseThrow(SurveyConvertException::new);
        return surveyInfoDTO;
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

    @Transactional
    public void deleteSurveyImage(String imageUrl) {
        s3Uploader.delete(imageUrl,DIRECTORY);
    }
}
