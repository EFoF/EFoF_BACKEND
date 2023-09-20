package com.service.surveyservice.domain.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // 해당 클래스에 Auditing 기능을 포함
@Getter
@MappedSuperclass // 자식 클래스에 매핑 정보만 제공
public class BaseTimeEntity {

    @CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate  // 조회한 Entity 값을 변경할 때 시간이 자동 저장
    private LocalDateTime modifiedDate;
}
