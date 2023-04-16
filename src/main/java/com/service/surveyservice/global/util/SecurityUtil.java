package com.service.surveyservice.global.util;

import com.service.surveyservice.global.error.exception.NotAuthorizedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new NotAuthorizedException();
        }
        return Long.parseLong(authentication.getName());
    }


    /**
     * 회원과 비회원 모두 같은 기능을 수행할 수 있지만
     * 해당 기능 내에서 세부적인 요소가 달라질 경우
     * ex :
     *  - 회원의 경우 설문조사 참여에서 좋아요 버튼이 활성된다.
     *  - 비회원의 경우 설문조사 조회만 할 수 있다.
     * 비회원이라고 예외가 발생해버리면 위 기능을 구현할 수 없다.
     * 따라서 아래의 메서드를 구현한다.
     * @return null || currentMemberId
     */
    public static Long getCurrentNullableMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            // 예외 발생이 아니라 null 값을 반환한다.
            // TODO null을 반환하기 보다는 임시 권한을 부여하는 식으로의 조치가 필요해보인다.
            return null;
        }
        return Long.parseLong(authentication.getName());
    }
}
