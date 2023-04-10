//package com.service.surveyservice.domain.member.dto;
//
//
//import com.service.surveyservice.domain.member.model.Member;
//import com.service.surveyservice.domain.member.model.MemberLoginType;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//public class MemberDTO {
//
//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SignUpRequest {
//        private String userName;
//
//        private String nickname;
//
//        private String email;
//
//        private String password;
//
//        private String organization;
//
//        private String organizationDetail;
//
//        private MemberLoginType memberLoginType;
//
//        public void encrypt(PasswordEncoder passwordEncoder) {
//            this.password = passwordEncoder.encode(password);
//        }
//
////        public Member toEntity() {
////
////            return Member.builder()
////                    .username(userName)
////                    .nickname(nickname)
////                    .phoneNumber(phoneNumber)
////                    .description("")
////                    .birthDate(birthDate)
////                    .email(email)
////                    .password(password)
////                    .userLoginType(memberLoginType)
////                    .build();
////        }
//    }
//}
