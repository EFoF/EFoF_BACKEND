package com.service.surveyservice.survey;
import com.service.surveyservice.domain.constraintoptions.dao.ConstraintRepository;
import com.service.surveyservice.domain.constraintoptions.dao.LocationConstraintRepository;
import com.service.surveyservice.domain.member.dao.MemberRepository;
import com.service.surveyservice.domain.member.model.Member;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyMemberMisMatchException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyNotFoundException;
import com.service.surveyservice.domain.survey.exception.exceptions.SurveyPreMisMatchException;
import com.service.surveyservice.domain.survey.model.ReleaseStatus;
import com.service.surveyservice.domain.survey.model.Survey;
import com.service.surveyservice.global.config.S3Config;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {


    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private ConstraintRepository constraintRepository;

    @Mock
    private S3Config s3Uploader;

    @Mock
    private LocationConstraintRepository locationConstraintRepository;
    private Member testMember;
    private Survey mockSurvey;
    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .email("email@example.com")
                .username("testName")
                .password("encryptedPassword")
                .nickname("oldNickname").build();

        mockSurvey = Survey.builder()
                .id(10L)
                .author(testMember)
                .title("Test Survey")
                .description("This is a test survey")
                .releaseStatus(ReleaseStatus.OVER)
                .build();
    }
    @Test
    void createSurveyTest() {
        // given
        SurveyDTO.SaveSurveyRequestDto saveSurveyRequestDto =
                new SurveyDTO.SaveSurveyRequestDto("Test Survey", "This is a test survey", null, null, null, null, null);
        when(memberRepository.findById(any())).thenReturn(java.util.Optional.of(testMember));
        when(surveyRepository.save(any())).thenReturn(new Survey());

        // when
        Survey createdSurvey = surveyService.createSurvey(saveSurveyRequestDto, testMember.getId());

        // then
        assertThat(createdSurvey).isNotNull();
    }
    @Test
    void getSurveyDataPreReleaseTest() {
        // given
        Long memberId = 1L;
        Long surveyId = 10L;

        Survey mockSurvey = Survey.builder()
                .id(surveyId)
                .author(testMember)
                .title("Test Survey")
                .description("This is a test survey")
                .releaseStatus(ReleaseStatus.PRE_RELEASE)
                .build();

        SurveyDTO.SurveySectionQueryDTO mockResponse = new SurveyDTO.SurveySectionQueryDTO();

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));
        when(surveyRepository.findSurveyBySurveyId(any())).thenReturn(mockResponse);

        // when
        SurveyDTO.SurveySectionQueryDTO surveySectionQueryDTO = surveyService.getSurveyDataPreRelease(memberId, surveyId);

        // then
        assertThat(surveySectionQueryDTO).isNotNull();
    }

    @Test
    void getSurveyDataPreReleaseNotFoundTest() {
        // given
        when(surveyRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(SurveyNotFoundException.class)
                .isThrownBy(() -> surveyService.getSurveyDataPreRelease(1L, 10L));
    }

    @Test
    void getSurveyDataPreReleaseMemberMismatchTest() {
        // given
        when(surveyRepository.findById(any())).thenReturn(Optional.of(Survey.builder().author(Member.builder().id(2L).build()).build()));

        // then
        assertThatExceptionOfType(SurveyMemberMisMatchException.class)
                .isThrownBy(() -> surveyService.getSurveyDataPreRelease(1L, 10L));
    }

    @Test
    void getSurveyDataPreReleaseStatusMismatchTest() {
        // given


        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // then
        assertThatExceptionOfType(SurveyPreMisMatchException.class)
                .isThrownBy(() -> surveyService.getSurveyDataPreRelease(1L, 10L));
    }

    @Test
    public void testGetSurveyData() {
        // Test data

        SurveyDTO.SurveySectionQueryDTO expectedSurveySectionQueryDTO = SurveyDTO.SurveySectionQueryDTO.builder().id(mockSurvey.getId()).build();

        // Mock behavior
        when(surveyRepository.findById(mockSurvey.getId())).thenReturn(Optional.of(mockSurvey));
        when(surveyRepository.findSurveyBySurveyId(mockSurvey.getId())).thenReturn(expectedSurveySectionQueryDTO);

        // Call the method to test
        SurveyDTO.SurveySectionQueryDTO result = surveyService.getSurveyData(testMember.getId(), mockSurvey.getId());

        // Verify the behavior and assert the result
        assertEquals(expectedSurveySectionQueryDTO, result);
    }

    @Test
    public void testGetSurveyData_SurveyNotFoundException() {
        // Test data

        // Mock behavior
        when(surveyRepository.findById(mockSurvey.getId())).thenReturn(Optional.empty());

        // Call the method to test and capture the exception
        assertThrows(SurveyNotFoundException.class, () -> surveyService.getSurveyData(testMember.getId(), mockSurvey.getId()));
    }

    @Test
    public void testGetSurveyData_SurveyMemberMisMatchException() {
        // Test data

        when(surveyRepository.findById(mockSurvey.getId())).thenReturn(Optional.ofNullable(mockSurvey));

        // Call the method to test and capture the exception
        assertThrows(SurveyMemberMisMatchException.class, () -> surveyService.getSurveyData(2L, mockSurvey.getId()));
    }

    @Test
    void updateSurveyTitleTest() {
        // given
        SurveyDTO.UpdateSurveyTextDto updateSurveyTextDto = new SurveyDTO.UpdateSurveyTextDto("New Title","new subscription");

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // when
        surveyService.updateSurveyTitle(updateSurveyTextDto, testMember.getId(), mockSurvey.getId());

        // then
        assertThat(mockSurvey.getTitle().equals("New Title"));
    }

    @Test
    void updateSurveyTitleNotFoundTest() {
        // given
        when(surveyRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(SurveyNotFoundException.class)
                .isThrownBy(() ->
                        surveyService.updateSurveyTitle(new SurveyDTO.UpdateSurveyTextDto("New Title","new subscription"), testMember.getId(), mockSurvey.getId()));
    }

    @Test
    void updateSurveyTitleMismatchTest() {
        // given
        when(surveyRepository.findById(any())).thenReturn(Optional.of(Survey.builder().author(Member.builder().id(2L).build()).build()));

        // then
        assertThatExceptionOfType(SurveyMemberMisMatchException.class)
                .isThrownBy(() ->
                        surveyService.updateSurveyTitle(new SurveyDTO.UpdateSurveyTextDto("New Title","new subscription"), testMember.getId(), mockSurvey.getId()));
    }

// 아래 메서드는 updateSurveyDescription(), updateSurveyColor(), updateSurveyOpenDate(), updateSurveyExpireDate() 에 대한 테스트와 예외 테스트입니다.
// 각 메서드에 대해 코드 작성 방식은 동일합니다. 수정하려는에 따라 dto 객체를 변경하고, testSurvey 객체에 대해 호출되는 메서드를 변경하면 됩니다.

    @Test
    void updateSurveyDescriptionTest() {
        // given
        SurveyDTO.UpdateSurveyTextDto updateSurveyTextDto = new SurveyDTO.UpdateSurveyTextDto("New Title","new subscription");

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // when
        surveyService.updateSurveyDescription(updateSurveyTextDto, testMember.getId(), mockSurvey.getId());

        // then
        assertThat(mockSurvey.getTitle().equals("new subscription"));
    }

// updateSurveyDescription()에 대한 NotFound, Mismatch 테스트 코드 작성 (updateSurveyTitleNotFoundTest(), updateSurveyTitleMismatchTest()와 비슷)

    @Test
    void updateSurveyColorTest() {
        // given
        Long memberId = 1L;
        Long surveyId = 10L;
        SurveyDTO.UpdateSurveyColorDto updateSurveyColorDto = new SurveyDTO.UpdateSurveyColorDto("#FFFF00","#FFFF00","#FFFF00");

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // when
        surveyService.updateSurveyColor(updateSurveyColorDto, memberId, surveyId);

        // then
        assertThat(mockSurvey.getBgColor().equals("#FFFF00"));
        assertThat(mockSurvey.getBtColor().equals("#FFFF00"));
        assertThat(mockSurvey.getFontColor().equals("#FFFF00"));
    }

// updateSurveyColor()에 대한 NotFound, Mismatch 테스트 코드 작성 (updateSurveyTitleNotFoundTest(), updateSurveyTitleMismatchTest()와 비슷)

    @Test
    void updateSurveyOpenDateTest() {
        // given
        LocalDateTime newOpenDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newExpireDate = LocalDateTime.now().plusDays(1);
        SurveyDTO.UpdateSurveyDateDto updateSurveyDateDto = new SurveyDTO.UpdateSurveyDateDto(newOpenDate,newExpireDate);

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // when
        surveyService.updateSurveyOpenDate(updateSurveyDateDto, testMember.getId(), mockSurvey.getId());

        // then
        assertThat(mockSurvey.getOpenDate().equals(newOpenDate));
    }

// updateSurveyOpenDate()에 대한 NotFound, Mismatch 테스트 코드 작성 (updateSurveyTitleNotFoundTest(), updateSurveyTitleMismatchTest()와 비슷)

    @Test
    void updateSurveyExpireDateTest() {
        // given
        LocalDateTime newOpenDate = LocalDateTime.now().plusDays(1);
        LocalDateTime newExpireDate = LocalDateTime.now().plusDays(1);
        SurveyDTO.UpdateSurveyDateDto updateSurveyDateDto = new SurveyDTO.UpdateSurveyDateDto(newOpenDate,newExpireDate);

        when(surveyRepository.findById(any())).thenReturn(Optional.of(mockSurvey));

        // when
        surveyService.updateSurveyExpireDate(updateSurveyDateDto, testMember.getId(), mockSurvey.getId());

        // then
        assertThat(mockSurvey.getExpireDate().equals(newExpireDate));
    }

}
