package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicPointRepository extends JpaRepository<CharacteristicPoint, Long> {

    List<CharacteristicPoint> findAllByAccountIsLike(Account account);

    @Query(value = "select  * from characteristic_point where is_public is true", nativeQuery = true)
    List<CharacteristicPoint> findAllPublicPoints();


}
