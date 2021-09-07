package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {

    Player getById(Long id);

    void save(Player player);

    void delete(Long id);

    List<Player> getAll();

    List<Player> getPlayersByNameAndTitle(String name, String title);

}
