package com.patrol.domain.facility.repository;


import com.patrol.domain.facility.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


public interface HospitalRepository extends JpaRepository<Hospital, Long> {

}
