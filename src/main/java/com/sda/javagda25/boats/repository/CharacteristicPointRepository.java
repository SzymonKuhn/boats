package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.CharacteristicPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicPointRepository extends JpaRepository<CharacteristicPoint, Long> {
}
