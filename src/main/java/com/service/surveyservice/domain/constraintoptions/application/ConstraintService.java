package com.service.surveyservice.domain.constraintoptions.application;

import com.service.surveyservice.domain.constraintoptions.dao.ConstraintRepository;
import com.service.surveyservice.domain.constraintoptions.dao.LocationConstraintRepository;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.ConstraintType;
import com.service.surveyservice.domain.constraintoptions.model.LocationConstraintOptions;
import com.service.surveyservice.domain.survey.dao.SurveyRepository;
import com.service.surveyservice.domain.survey.model.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConstraintService {
    private final ConstraintRepository constraintRepository;
    private final LocationConstraintRepository locationConstraintRepository;
    private final SurveyRepository surveyRepository;

    @Transactional(readOnly = true)
    public List<ConstraintTypeDTO> getLocations(ConstraintTypeDTO constraintTypeDTO) {
        //GPS Constraint가 걸려있는 녀석 찾음
        List<ConstraintOptions> typeGPS = constraintRepository.findByConstraintType(ConstraintType.GPS_CONSTRAINT);

        //사용자 위치 위도, 경도값 받아옴.
        Float locationLat = constraintTypeDTO.getLat();
        Float locationLng = constraintTypeDTO.getLng();

        //lat, lng, surveyId 담을 List 생성
        List<ConstraintTypeDTO> constraintTypeDTOList = new ArrayList<>();

        for (ConstraintOptions typeGPSEach : typeGPS) {
            Optional<LocationConstraintOptions> GPS = locationConstraintRepository.findByConstraintOptions(typeGPSEach);
            Long surveyId = typeGPSEach.getSurvey().getId();
            Optional<Survey> survey = surveyRepository.findById(surveyId);

            String title = survey.get().getTitle();
            String sImageURL = survey.get().getSImageURL();


            Float lat = GPS.get().getLatitude();
            Float lng = GPS.get().getLongitude();

            // 1km 반경 내에 있는지 확인
            double distance = calculateDistance(locationLat, locationLng, lat, lng);

            if (distance <= 1) {
                constraintTypeDTOList.add(constraintTypeDTO.builder()
                                .lat(lat)
                                .lng(lng)
                                .id(surveyId)
                                .title(title)
                                .sImageURL(sImageURL)
                        .build());
            }
        }

        return constraintTypeDTOList;
    }

    public double calculateDistance(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371; // 지구의 평균 반경 (km 단위)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
}
