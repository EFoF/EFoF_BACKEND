package com.service.surveyservice.domain.member.application;

import com.service.surveyservice.domain.member.exception.exceptions.mail.EmailCertificationExpireException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.service.surveyservice.global.common.constants.EmailConstants.EMAIL_CERTIFICATION_TIME;
import static javax.mail.Message.RecipientType.TO;
import static com.service.surveyservice.domain.member.dto.MailDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCertificationService {
    private final JavaMailSender emailSender;
    private final RedisTemplate<String, String> redisTemplate;

    private String ePw;

    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(TO, to);
        message.setSubject("독설 이메일 인증");

        String msg = "";
        msg += "<div style = 'margin:100px;'>";
        msg += "<h1> 안녕하세요 </h1>";
        msg += "<h1> 설문 플랫폼 독설 입니다.</h1>";
        msg += "<br>";
        msg += "<p>서비스 내에서 아래 코드를 입력해주세요</p>";
        msg += "<br>";
        msg += "<div align='center' style = 'border:1px solid black; font-family:verdana';>";
        msg += "<h3 style = 'color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-style:130%'>";
        msg += "CODE: <strong>";
        msg += ePw + "</strong><div><br/>";
        msg += "</div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("oldstyle4@naver.com", "DOKSEOL_admin"));
        return message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }

    // 메일 발송
    public void sendSimpleMessage(String to) throws Exception {
        ePw = createKey();
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
            log.info("secret code = " + ePw);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(to, ePw);
            redisTemplate.expire(to, EMAIL_CERTIFICATION_TIME, TimeUnit.MILLISECONDS);
        } catch (MailException es) {
            log.info(es.getLocalizedMessage());
            throw new IllegalArgumentException(es.getMessage());
        }

    }

    // 코드 검증
    public CodeConfirmDTO confirmCode(EmailConfirmCodeDTO emailConfirmCodeDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get(emailConfirmCodeDto.getEmail());
        if (code == null) {
            throw new EmailCertificationExpireException();
        }
        if (!code.equals(emailConfirmCodeDto.getCode())) {
            return CodeConfirmDTO.builder().matches(false).build();
        }
        return CodeConfirmDTO.builder().matches(true).build();

    }
}
