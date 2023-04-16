package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;
import static com.service.surveyservice.domain.survey.dto.SurveyDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

   private final MemberService memberService;
   private final SurveyService surveyService;
   private final SurveyRepository surveyRepository;

   private final MemberRepository memberRepository;
   private final MemberCustomRepositoryImpl memberCustomRepository;

   // 닉네임 변경
    @PatchMapping(value = "/member/update/nickname")
    public ResponseEntity<String> updateMemberNickname(@RequestBody UpdateNicknameRequestDTO updateNicknameRequestDTO) {
        String result = memberService.updateMemberNickname(updateNicknameRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 프로필 이미지 변경
    @PatchMapping(value = "/member/update/profile")
    public ResponseEntity<String> updateMemberProfile(@RequestBody UpdateMemberProfileImgRequestDTO updateMemberProfileImgRequestDTO) {
        String result = memberService.updateMemberProfileImg(updateMemberProfileImgRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

  // 비밀번호 변경
    @PatchMapping(value = "/member/update/password")
    public ResponseEntity<String> updateMemberPassword(@RequestBody UpdateMemberPasswordRequestDTO updateMemberPasswordRequestDTO) {
        String result = memberService.updatePassword(updateMemberPasswordRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

   // 특정 사용자 정보 조회
   @GetMapping(value = "/member/{memberId}")
    public ResponseEntity<MemberDetail> getMemberDetail(@PathVariable long memberId) {
       return new ResponseEntity<>(memberService.getMemberDetail(memberId), HttpStatus.OK);
   }

   // 특정 사용자가 생성한 설문조사 조회 - 페이지네이션 적용
//    @GetMapping(value = "/member/form/author/{memberId}")
//    public ResponseEntity<Page<SurveyInfoDTO>> getAuthorSurveyInfo(@PathVariable(name = "memberId") Long memberId, Pageable pageable) {
//
//    }

}
