
package com.service.surveyservice.domain.constraintoptions.dao;

import com.service.surveyservice.domain.constraintoptions.model.ConstraintOptions;
import com.service.surveyservice.domain.constraintoptions.model.LocationConstraintOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationConstraintRepository extends JpaRepository<LocationConstraintOptions, Long> {

    @Query(value = "select lco from LocationConstraintOptions lco where lco.constraintOptions.id = :constraint_options_id")
    LocationConstraintOptions findByConstraintOptionId(@Param("constraint_options_id")Long constraint_options_id);
}
