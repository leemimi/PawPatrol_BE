package com.patrol.domain.protection.facility.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingHours {

  private String weekdayTime;  // 평일 운영시간 (예: 09:00 - 19:00)
  private String weekendTime;  // 주말 운영시간 (예: 09:00 - 13:00)
  private String closedDays;   // 휴무일 (예: 매주 일요일)

}
