package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.transport.ObjectTable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames(){
        return gameRepository.findAll().stream().map(game -> this.gamesDTO(game)).collect(Collectors.toList());
    }

    public Map<String, Object> gamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> this.gamePlayersDTO(gamePlayer)).collect(Collectors.toList()));
        return dto;
    }

    public Map<String, Object> gamePlayersDTO(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", this.playersDTO(gamePlayer.getPlayer()));
        return dto;
    }

    public Map<String, Object> playersDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id",player.getId());
        dto.put("email",player.geteMail());
        return dto;
    }
}
