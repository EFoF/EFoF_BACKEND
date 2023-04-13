package com.service.surveyservice.domain.member.api;

import com.service.surveyservice.domain.member.application.MemberService;
import com.service.surveyservice.domain.member.dao.MemberCustomRepositoryImpl;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.service.surveyservice.domain.member.dto.MemberDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

   private final MemberService memberService;
   private final MemberRepository memberRepository;
   private final MemberCustomRepositoryImpl memberCustomRepository;


   @GetMapping(value = "/member/{memberId}")
    public ResponseEntity<MemberDetail> getMemberDetail(@PathVariable long memberId) {
       return new ResponseEntity<>(memberService.getMemberDetail(memberId), HttpStatus.OK);
   }
}
