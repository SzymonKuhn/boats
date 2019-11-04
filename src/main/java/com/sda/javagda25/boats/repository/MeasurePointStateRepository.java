package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.MeasurePointState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MeasurePointStateRepository extends JpaRepository <MeasurePointState, Long> {
    boolean existsByIdStationAndMeasureDateTime (Long idStation, LocalDateTime localDateTime);

    @Query (value = "select id, measure_point_id, water_state, id_station, max(measure_date_time) as measure_date_time from measure_point_state group by measure_point_id", nativeQuery = true)
    List<MeasurePointState> findLatestStates();

    @Query (value = "select id, measure_point_id, water_state, id_station, max(measure_date_time) as measure_date_time from measure_point_state where measure_point_id = :measurePointId", nativeQuery = true)
    Optional<MeasurePointState> findLatestStateOfMeasurePoint(@Param("measurePointId") Long measurePointId);
}
