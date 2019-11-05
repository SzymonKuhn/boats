package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.repository.CharacteristicPointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacteristicPointService {

    private CharacteristicPointRepository characteristicPointRepository;

    public CharacteristicPointService(CharacteristicPointRepository characteristicPointRepository) {
        this.characteristicPointRepository = characteristicPointRepository;
    }

    public List<CharacteristicPoint> findAllCharacteristicPoints() {
        return characteristicPointRepository.findAll();
    }

    public void save(CharacteristicPoint characteristicPoint) {
        characteristicPointRepository.save(characteristicPoint);
    }
}
