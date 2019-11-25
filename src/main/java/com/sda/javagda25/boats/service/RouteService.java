package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.model.Route;
import com.sda.javagda25.boats.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;


    public Long save (Route route) {
        Route savedRoute = routeRepository.save(route);
        return savedRoute.getId();
    }

    public Route getById (Long id) {
        Optional<Route> optional = routeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public List<Route> getAll () {
        return routeRepository.findAll();
    }

    public List<Route> findAllByAccountIsLike(Account account) {
        return routeRepository.findAllByAccountIsLike(account);
    }

    public List<Route> findAllPublicRoutes() {
        return routeRepository.findAllPublicRoutes();
    }

    public void delete(Route route) {
        routeRepository.delete(route);
    }
}
