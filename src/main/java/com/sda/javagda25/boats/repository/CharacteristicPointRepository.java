package com.sda.javagda25.boats.repository;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacteristicPointRepository extends JpaRepository<CharacteristicPoint, Long> {

    List<CharacteristicPoint> findAllByAccountIsLike(Account account);

    @Query(value = "select  * from characteristic_point where is_public is true", nativeQuery = true)
    List<CharacteristicPoint> findAllPublicPoints();

    @Query("select cp from CharacteristicPoint cp where (name like %:search%) and (account=:account or isPublic=true)")
    Page<CharacteristicPoint> findAllUserPointsOrPublicPoints(@Param("account") Account account, @Param("search") String search, Pageable pageable);

    @Query("select cp from CharacteristicPoint cp where (name like %:search%) and (isPulic=true)")
    Page<CharacteristicPoint> findPublicPointsByName(@Param("search") String name, Pageable pageable);


}
