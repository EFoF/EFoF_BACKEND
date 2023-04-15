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


  // 비밀번호 변경
  @PostMapping(value = "/member/update/password")
  public ResponseEntity<String> updateMemberPassword(@RequestBody UpdateMemberPasswordRequestDTO updateMemberPasswordRequestDTO) {
        String result = memberService.updatePassword(updateMemberPasswordRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
  }

   // 특정 사용자 정보 조회
   @GetMapping(value = "/member/{memberId}")
    public ResponseEntity<MemberDetail> getMemberDetail(@PathVariable long memberId) {
       return new ResponseEntity<>(memberService.getMemberDetail(memberId), HttpStatus.OK);
   }

   // 설문 참여 사용자 조회 - 페이지네이션
   //

}
