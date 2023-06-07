package com.service.surveyservice.domain.constraintoptions.api;

import com.service.surveyservice.domain.constraintoptions.application.ConstraintService;
import com.service.surveyservice.domain.survey.application.SurveyService;
import com.service.surveyservice.domain.survey.dto.SurveyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.service.surveyservice.domain.constraintoptions.dto.ConstraintDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConstraintController {
    private final ConstraintService constraintService;
    private final SurveyService surveyService;

    /**
     *
     * @param constraintTypeDTO
     * @return List<ConstraintTypeDTO>
     * GPS Constraint 걸려있는 Survey 조사
     */
    @PostMapping(value = "/gps")
    private List<ConstraintTypeDTO> getLocations(@RequestBody ConstraintTypeDTO constraintTypeDTO){
        List<ConstraintTypeDTO> locations = constraintService.getLocations(constraintTypeDTO);

        return locations;
    }
}
