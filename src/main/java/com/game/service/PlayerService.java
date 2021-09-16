package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {

    Player getById(Long id);

    void save(Player player);

    void delete(Long id);

    List<Player> getAll();
}
