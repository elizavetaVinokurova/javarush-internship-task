package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("")
    public ResponseEntity<List<Player>> getAllPlayers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") Race race,
            @RequestParam(required = false, defaultValue = "") Profession profession,
            @RequestParam(required = false, defaultValue = "") Boolean banned,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize
    ) {
        List<Player> players = playerRepository.findAllWithFilters(name, title,
                race, profession, banned, after, before, minExperience, maxExperience,
                minLevel, maxLevel, order, pageNumber, pageSize);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }


    @GetMapping(value = "/count")
    public ResponseEntity<Integer> getAllCount(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") Race race,
            @RequestParam(required = false, defaultValue = "") Profession profession,
            @RequestParam(required = false, defaultValue = "") Boolean banned,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel
    ) {

        Integer count = Math.toIntExact(playerRepository.getPlayersCount(name, title, race, profession, banned,
                after, before, minExperience, maxExperience, minLevel, maxLevel));
        return new ResponseEntity<>(count, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long playerId) {
        if (playerId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = this.playerService.getById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long playerId) {
        if (playerId > this.playerService.getAll().size()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (playerId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        this.playerService.delete(playerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {

        if (player.getName() == null ||
            player.getTitle() == null ||
            player.getRace() == null ||
            player.getProfession() == null ||
            player.getBirthday() == null ||
            player.getExperience() == null
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getName().length() > 12 || player.getName().equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getTitle().length() > 30) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LocalDate localDate = player.getBirthday().toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
        if (player.getBirthday().getTime() < 0 || localDate.getYear() < 2000 || localDate.getYear() > 3000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (player.getBanned() == null) {
            player.setBanned(false);
        }

        //LEVEL
        Double tmpLevel = ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
        Integer level = tmpLevel.intValue();
        player.setLevel(level);
        //UNTIL NEXT LEVEL
        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
        player.setUntilNextLevel(untilNextLevel);

        player.setId(null);

        this.playerService.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long playerId, @RequestBody Player playerDto) {
        if (playerId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player player = this.playerService.getById(playerId);
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (playerDto.getName() != null) {
            player.setName(playerDto.getName());
        }

        if (playerDto.getTitle() != null) {
            player.setTitle(playerDto.getTitle());
        }

        if (playerDto.getRace() != null) {
            player.setRace(playerDto.getRace());
        }

        if (playerDto.getProfession() != null) {
            player.setProfession(playerDto.getProfession());
        }

        if (playerDto.getBirthday() != null) {

            LocalDate localDate = playerDto.getBirthday().toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
            if (playerDto.getBirthday().getTime() < 0 || localDate.getYear() < 2000 || localDate.getYear() > 3000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            player.setBirthday(playerDto.getBirthday());
        }

        if (playerDto.getBanned() != null) {
            player.setBanned(playerDto.getBanned());
        }

        if (playerDto.getExperience() != null) {

            if (playerDto.getExperience() < 0 || playerDto.getExperience() > 10_000_000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            player.setExperience(playerDto.getExperience());

            //LEVEL
            Double tmpLevel = ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
            Integer level = tmpLevel.intValue();
            player.setLevel(level);
            //UNTIL NEXT LEVEL
            Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
            player.setUntilNextLevel(untilNextLevel);
        }

        this.playerService.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

}
