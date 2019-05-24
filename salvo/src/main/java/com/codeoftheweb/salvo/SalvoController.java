package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
class SalvoController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/owners/{ownerId}")
    public Map <String, Object> findOwner(@PathVariable Long ownerId) {
        Map <String, Object> map = new HashMap<>();
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(ownerId);
        if(optionalGamePlayer.isPresent()) {
            GamePlayer gamePlayer = optionalGamePlayer.get();
            map = this.game_viewDTO(gamePlayer);
        }
        else {
            map.put("error", "no se encuentra el player");
        }
        return map;
    }

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames(){
        return gameRepository.findAll().stream().map(this::gamesDTO).collect(Collectors.toList());
    }

    private Map<String, Object> game_viewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(this::ShipDTO));
        return dto;
    }

    private Map<String, Object> gamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> gamePlayersDTO(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", this.playersDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> playersDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",player.getId());
        dto.put("email",player.geteMail());
        return dto;
    }

    private Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("locations", ship.getLocations());
        dto.put("type", ship.getShipType());
        return dto;
    }
}
