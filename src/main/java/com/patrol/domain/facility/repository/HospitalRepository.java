package com.patrol.domain.facility.repository;


import com.patrol.domain.facility.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HospitalRepository extends JpaRepository<Hospital, Long> {

}
