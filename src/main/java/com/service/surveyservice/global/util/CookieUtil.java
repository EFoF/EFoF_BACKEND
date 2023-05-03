package com.service.surveyservice.global.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    /**
     *
     * @param request
     * @param name
     * @return Cookie(Optional)
     * 사용자의 요청에서 쿠키가 있다면 쿠키를 얻어옴
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                if(name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * @param response
     * @param name
     * @param value
     * @param maxAge
     * 사용자에게 보낼 응답에 쿠키를 실어서 보냄
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(name, value);
        // cookie의 path를 지정해줌. 매개변수로 지정된 디렉토리와 그 하위에서만 쿠키를 생성할 수 있음
        cookie.setPath("/");
        cookie.setHttpOnly(isHttpOnly);
        cookie.setMaxAge(maxAge);

        // 응답에 쿠키를 추가
       response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for( Cookie cookie : cookies) {
                if( name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
