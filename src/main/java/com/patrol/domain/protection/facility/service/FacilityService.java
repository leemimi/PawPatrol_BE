package com.patrol.domain.protection.facility.service;

import com.patrol.api.protection.facility.dto.FacilitiesResponse;

import java.util.List;

public interface FacilityService {
  List<FacilitiesResponse> findAll();
}
