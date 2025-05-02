package com.digitaltimecapsule.digitaltimecapsule.repository;

import com.digitaltimecapsule.digitaltimecapsule.model.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByOpenedFalseAndExpiryDateTimeBefore(LocalDateTime now);

    List<Capsule> findByOpenedFalse();

    List<Capsule> findByOpenedTrue();
}