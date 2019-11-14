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
public interface MeasurePointStateRepository extends JpaRepository<MeasurePointState, Long> {
    boolean existsByIdStationAndMeasureDateTime(Long idStation, LocalDateTime localDateTime);

    List<MeasurePointState> findAllByMeasurePointOrderByMeasureDateTimeDesc(MeasurePoint measurePoint);

    @Query(value = "select id, measure_point_id, water_state, id_station, measure_date_time from measure_point_state where measure_date_time = (select max(measure_date_time) from measure_point_state where id = id)", nativeQuery = true)
    List<MeasurePointState> findLatestStates();

    @Query(value = "select  id, measure_point_id, water_state, id_station, measure_date_time from measure_point_state where measure_point_id = :measurePointId order by measure_date_time desc limit 1", nativeQuery = true)
    Optional<MeasurePointState> findLatestStateOfMeasurePoint(@Param("measurePointId") Long measurePointId);
}
