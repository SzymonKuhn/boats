package com.sda.javagda25.boats.service;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.repository.CharacteristicPointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CharacteristicPointService {

    private CharacteristicPointRepository characteristicPointRepository;

    public CharacteristicPointService(CharacteristicPointRepository characteristicPointRepository) {
        this.characteristicPointRepository = characteristicPointRepository;
    }

    public List<CharacteristicPoint> findAllByAccountIsLike(Account account) {
        return characteristicPointRepository.findAllByAccountIsLike(account);
    }

    public void save(CharacteristicPoint characteristicPoint) {
        characteristicPointRepository.save(characteristicPoint);
    }

    public List<CharacteristicPoint> getAllPublicPoints() {
        return characteristicPointRepository.findAllPublicPoints();
    }

    public CharacteristicPoint getById(Long id) {
        Optional<CharacteristicPoint> optional = characteristicPointRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Page<CharacteristicPoint> publicCharacteristicPointPage(String search, PageRequest pageRequest) {
        return characteristicPointRepository.findPublicPointsByName(search, pageRequest);
    }

    public Page<CharacteristicPoint> userAndPublicCharacteristicPointPage(String search, PageRequest pageRequest, Account account) {
        return characteristicPointRepository.findAllUserPointsOrPublicPoints(account, search, pageRequest);
    }
}
