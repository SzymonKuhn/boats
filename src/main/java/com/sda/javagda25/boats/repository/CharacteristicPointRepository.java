package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicPointRepository extends JpaRepository<CharacteristicPoint, Long> {

    List<CharacteristicPoint> findAllByAccountIsLike(Account account);

}
