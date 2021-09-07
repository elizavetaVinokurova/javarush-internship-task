//package com.game.controller;
//
//import com.game.entity.Player;
//import com.game.entity.Profession;
//import com.game.entity.Race;
//import com.game.service.PlayerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/rest/players")
//public class PlayerControllerTest {
//
//    @Autowired
//    private PlayerService playerService;
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long playerId) {
//        if (playerId == null || playerId < 0) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Player player = this.playerService.getById(playerId);
//
//        if (player == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(player, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "", method = RequestMethod.POST)
//    public ResponseEntity<Player> savePlayer(@RequestBody Player playerDto) {
//
//        Player player = new Player();
//        //NAME
//        if (playerDto.getName() == null || playerDto.getName().length() > 12 || playerDto.getName().equals("")) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setName(playerDto.getName());
//        //TITLE
//        if (playerDto.getTitle() == null || playerDto.getTitle().length() > 30) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setTitle(playerDto.getTitle());
//        //RACE
//        if (playerDto.getRace() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setRace(playerDto.getRace());
//        //PROFESSION
//        if (playerDto.getProfession() == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setProfession(playerDto.getProfession());
//        //BIRTHDAY
//        if (playerDto.getBirthday() == null || playerDto.getBirthday().getTime() < 0 ||
//            playerDto.getBirthday().getYear() < 2000 || playerDto.getBirthday().getYear() > 3000) {
//
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setBirthday(playerDto.getBirthday());
//        //BANNED
//        player.setBanned(playerDto.getBanned());
//        //EXPERIENCE
//        if (playerDto.getExperience() < 0 || playerDto.getExperience() > 10_000_000) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        player.setExperience(playerDto.getExperience());
//        //LEVEL
//        Double tmpLevel =  ((Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100);
//        Integer level = tmpLevel.intValue();
//        player.setLevel(level);
//        //UNTIL NEXT LEVEL
//        Integer untilNextLevel = 50 * (level + 1) * (level + 2) - player.getExperience();
//        player.setUntilNextLevel(untilNextLevel);
//
//        this.playerService.save(player);
//        return new ResponseEntity<>(player, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    public ResponseEntity<Player> updatePlayer(@RequestBody Player playerDto, @PathVariable("id") Long playerId) {
//        if (playerId == null || playerId < 0) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Player player = this.playerService.getById(playerId);
//
//        if (player == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        if (playerDto.getName() != null) {
//            player.setName(playerDto.getName());
//        }
//        if (playerDto.getTitle() != null) {
//            player.setTitle(playerDto.getTitle());
//        }
//        if (playerDto.getRace() != null) {
//            player.setRace(playerDto.getRace());
//        }
//        if (playerDto.getProfession() != null) {
//            player.setProfession(playerDto.getProfession());
//        }
//        if (playerDto.getBirthday() != null) {
//            player.setBirthday((playerDto.getBirthday()));
//        }
//        if (playerDto.getBanned() != null) {
//            player.setBanned(playerDto.getBanned());
//        }
//        if (playerDto.getExperience() != null) {
//            player.setExperience(playerDto.getExperience());
//        }
//
//        this.playerService.save(player);
//
//        return new ResponseEntity<>(player, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long playerId) {
//        if (playerId == null || playerId < 0 ) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Player player = this.playerService.getById(playerId);
//
//        if (player == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        this.playerService.delete(playerId);
//
//        return new ResponseEntity<>(player, HttpStatus.OK);
//    }
//
//
//
//    @GetMapping("")
//    public ResponseEntity<List<Player>> getPlayers(
//            @RequestParam(required = false, defaultValue = "") String name,
//            @RequestParam(required = false, defaultValue = "") String title
//    ) {
//        List<Player> players = this.playerService.getPlayersByNameAndTitle(name, title);
//
//        return new ResponseEntity<>(players, HttpStatus.OK);
//    }
//}
