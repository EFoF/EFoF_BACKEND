package com.service.surveyservice.domain.section.dao;

import com.service.surveyservice.domain.section.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long>, SectionCustomRepository {
}
