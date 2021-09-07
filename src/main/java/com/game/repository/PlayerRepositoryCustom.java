package com.game.repository;

import com.game.entity.Player;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepositoryCustom {
    List<Player> getPlayersByNameAndTitle(String name, String title);
}
