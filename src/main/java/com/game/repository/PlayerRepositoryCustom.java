package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerRepositoryCustom {
    List<Player> findAllWithFilters(String name, String title, Race race, Profession profession, Boolean banned,
                                    Long after, Long before, Integer minExperience, Integer maxExperience,
                                    Integer minLevel, Integer maxLevel,
                                    PlayerOrder order, Integer pageNumber, Integer pageSize);

    long getPlayersCount(String name, String title, Race race, Profession profession, Boolean banned, Long after,
                         Long before, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel);
}
