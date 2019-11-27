package com.sda.javagda25.boats.repository;


import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.MeasurePoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurePointRepository extends JpaRepository <MeasurePoint, Long>, PagingAndSortingRepository <MeasurePoint, Long> {
    boolean existsById(Long id);

    List<MeasurePoint> findAllByIdIsLike (Long id);
    List<MeasurePoint> findAllByPointNameIsLikeOrRiverNameIsLike (String name, String river);
    
}
