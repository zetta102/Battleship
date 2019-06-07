package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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

    @RequestMapping("/game_view/{nn}")
    public Map <String, Object> findOwner(@PathVariable Long nn) {
        Map <String, Object> map = new HashMap<>();
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(nn);
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
    public Map<String, Object> getGames() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("games", gameRepository.findAll().stream().map(this::gamesDTO).collect(Collectors.toList()));
        dto.put("stats", playerRepository.findAll().stream().map(this::playerStatisticsDTO).collect((Collectors.toList())));
        return dto;
    }

    private Map<String, Object> game_viewDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("view_id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(this::ShipDTO));
        dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(sgp -> sgp.getSalvoes().stream().map(this::salvoDTO)));
        dto.put("players", gamePlayer.getGame().getGamePlayers().stream().map(this::gameStatsDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> salvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turnNumber", salvo.getTurnNumber());
        dto.put("location", salvo.getLocations());
        return dto;
    }

    private Map<String, Object> gameStatsDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> gamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> gamePlayersDTO(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayer_id", gamePlayer.getId());
        dto.put("player", this.playersDTO(gamePlayer.getPlayer()));
        if (gamePlayer.getPlayer().getScore(gamePlayer.getGame()) != null)
            dto.put("score", gamePlayer.getPlayer().getScore(gamePlayer.getGame()).getScore());
        else
            dto.put("score", null);
        return dto;
    }

    private Map<String, Object> playersDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id", player.getId());
        dto.put("email",player.geteMail());
        return dto;
    }

    private Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("locations", ship.getLocations());
        dto.put("type", ship.getShipType());
        return dto;
    }

    public Map<String, Object> playerStatisticsDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        double total = player.getScores().stream().mapToDouble(Score::getScore).sum();
        double won = player.getScores().stream().filter(score -> score.getScore() == 1).count();
        double lost = player.getScores().stream().filter(score -> score.getScore() == 0).count();
        double tied = player.getScores().stream().filter(score -> score.getScore() == 0.5).count();
        dto.put("score", total);
        dto.put("won", won);
        dto.put("lost", lost);
        dto.put("tied", tied);
        return dto;
    }
}
