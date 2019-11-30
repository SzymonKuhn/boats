package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.MeasurePoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurePointRepository extends JpaRepository <MeasurePoint, Long>{
    boolean existsById(Long id);

    @Query ("select mp from MeasurePoint mp where ((point_name like %?1%) or (river_name like %?2%))")
    Page<MeasurePoint> findByPointNameOrRiverName (String name, String river, Pageable pageable);
}
