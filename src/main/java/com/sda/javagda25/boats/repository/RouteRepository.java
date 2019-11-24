package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository <Route, Long> {
    List<Route> findAllByAccountIsLike (Account account);

    @Query(value = "select  * from route where is_public is true", nativeQuery = true)
    List<Route> findAllPublicRoutes();
}
