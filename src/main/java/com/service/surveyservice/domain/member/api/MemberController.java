package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

   private final MemberService memberService;
   private final MemberRepository memberRepository;
   private final MemberCustomRepositoryImpl memberCustomRepository;

   // 닉네임 변경
    @PatchMapping(value = "/member/update/nickname")
    public ResponseEntity<String> updateMemberNickname(@RequestBody UpdateNicknameRequestDTO updateNicknameRequestDTO) {
        // TODO 아이디 조회 로직 추가
        String result = memberService.updateMemberNickname(updateNicknameRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 프로필 이미지 변경
    @PatchMapping(value = "/member/update/profile")
    public ResponseEntity<String> updateMemberProfile(@RequestBody UpdateMemberProfileImgRequestDTO updateMemberProfileImgRequestDTO) {
        // TODO 아이디 조회 로직 추가
        String result = memberService.updateMemberProfileImg(updateMemberProfileImgRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

  // 비밀번호 변경
    @PatchMapping(value = "/member/update/password")
    public ResponseEntity<String> updateMemberPassword(@RequestBody UpdateMemberPasswordRequestDTO updateMemberPasswordRequestDTO) {
        // TODO 아이디 조회 로직 추가
        String result = memberService.updatePassword(updateMemberPasswordRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

   // 특정 사용자 정보 조회
   @GetMapping(value = "/member/{memberId}")
    public ResponseEntity<MemberDetail> getMemberDetail(@PathVariable long memberId) {
       return new ResponseEntity<>(memberService.getMemberDetail(memberId), HttpStatus.OK);
   }


}
