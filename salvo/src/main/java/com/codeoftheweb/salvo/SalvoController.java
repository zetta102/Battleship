package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
class SalvoController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByeMail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gpId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placedShips(@PathVariable Long gpId, @RequestBody List<Ship> ships, Authentication authentication) {
        ResponseEntity<Map<String, Object>> responseEntity;
        Player player = playerRepository.findByeMail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpId).orElse(null);
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            responseEntity = new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            if (gamePlayer == null) {
                responseEntity = new ResponseEntity<>(makeMap("error", "that game does not exist"), HttpStatus.NOT_FOUND);
            } else {
                if (player.getId() != gamePlayer.getPlayer().getId()) {
                    responseEntity = new ResponseEntity<>(makeMap("error", "that player is not participating in that game"), HttpStatus.NOT_FOUND);
                } else {
                    if (gamePlayer.getShips().size() > 0) {
                        responseEntity = new ResponseEntity<>(makeMap("error", "you've already placed ships"), HttpStatus.NOT_FOUND);
                    } else {
                        if (ships.size() != 5) {
                            responseEntity = new ResponseEntity<>(makeMap("error", "you need 5 ships to play"), HttpStatus.FORBIDDEN);
                        } else {
                            ships.forEach(gamePlayer::addShip);
                            gamePlayerRepository.save(gamePlayer);
                            responseEntity = new ResponseEntity<>(makeMap("success", "the ships have been placed"), HttpStatus.CREATED);
                        }
                    }
                }
            }
        }
        return responseEntity;
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String, Object>> findOwner(@PathVariable Long nn, Authentication authentication) {
        ResponseEntity<Map<String, Object>> responseEntity;
        Optional<GamePlayer> optionalGamePlayer = gamePlayerRepository.findById(nn);
        if (optionalGamePlayer.isPresent()) {
            GamePlayer gamePlayer = optionalGamePlayer.get();
            if (isGuest(authentication)) {
                responseEntity = new ResponseEntity<>(makeMap("error", "unauthorized"), HttpStatus.UNAUTHORIZED);
            } else {
                Player player = playerRepository.findByeMail(authentication.getName());
                if (gamePlayer.getPlayer().getId() == player.getId()) {
                    responseEntity = new ResponseEntity<>(this.game_viewDTO(gamePlayer, authentication), HttpStatus.OK);
                } else {
                    responseEntity = new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.FORBIDDEN);
                }
            }
        } else {
            return new ResponseEntity<>(makeMap("error", "that player does not exist"), HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/games/{nn}/players", method = RequestMethod.POST)
    private ResponseEntity<Map<String, Object>> joinGames(Authentication authentication, @PathVariable long nn) {
        ResponseEntity<Map<String, Object>> responseEntity;
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            responseEntity = new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.UNAUTHORIZED);
        } else {
            Player playerId = playerRepository.findByeMail(authentication.getName());
            Game game = gameRepository.findById(nn).orElse(null);
            if (game == null) {
                responseEntity = new ResponseEntity<>(makeMap("error", "that game does not exist"), HttpStatus.BAD_REQUEST);
            } else {
                if (game.getPlayers().size() != 1) {
                    responseEntity = new ResponseEntity<>(makeMap("error", "the game is full"), HttpStatus.FORBIDDEN);
                } else {
                    if (game.getPlayers().stream().anyMatch(gp -> gp.getId() == playerId.getId())) {
                        responseEntity = new ResponseEntity<>(makeMap("error", "cannot play against yourself"), HttpStatus.FORBIDDEN);
                    } else {
                        Player player = playerRepository.findByeMail(authentication.getName());
                        GamePlayer gamePlayer = new GamePlayer(player, game, LocalDateTime.now());
                        gamePlayerRepository.save(gamePlayer);
                        responseEntity = new ResponseEntity<>(makeMap("gamePlayer_id", gamePlayer.getId()), HttpStatus.CREATED);
                    }
                }
            }
        }
        return responseEntity;
    }


    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            dto.put("player", "guest");
        } else {
            dto.put("player", this.playersDTO(playerRepository.findByeMail(authentication.getName())));
        }
        dto.put("games", gameRepository.findAll().stream().map(this::gamesDTO).collect(Collectors.toList()));
        dto.put("stats", playerRepository.findAll().stream().map(this::playerStatisticsDTO).collect(Collectors.toList()));
        return dto;
    }

    @PostMapping("games/players/{gpId}/salvoes")
    private ResponseEntity<Map<String, Object>> addSalvoes(Authentication authentication, @PathVariable long gpId, @RequestBody List<String> shots) {
        ResponseEntity<Map<String, Object>> responseEntity;
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpId).orElse((null));
        Player player = playerRepository.findByeMail(authentication.getName());
        if (gamePlayer == null) {
            responseEntity = new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.FORBIDDEN);
        } else if (gamePlayer.getPlayer().getId() != player.getId()) {
            responseEntity = new ResponseEntity<>(makeMap("error", "that is not your game"), HttpStatus.FORBIDDEN);
        } else if (shots.size() > 5) {
            responseEntity = new ResponseEntity<>(makeMap("error", "you're firing more salvoes than you should"), HttpStatus.FORBIDDEN);
        } else {
            int turn = gamePlayer.getSalvoes().size() + 1;
            Salvo newSalvo = new Salvo(turn, shots);
            gamePlayer.addSalvo(newSalvo);
            gamePlayerRepository.save(gamePlayer);
            responseEntity = new ResponseEntity<>(makeMap("success", "created"), HttpStatus.CREATED);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    private ResponseEntity<Map<String, Object>> createGames(Authentication authentication) {
        ResponseEntity<Map<String, Object>> responseEntity;
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            responseEntity = new ResponseEntity<>(makeMap("error", "not logged in"), HttpStatus.FORBIDDEN);
        } else {
            Player player = playerRepository.findByeMail(authentication.getName());
            Game game = gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer gamePlayer = new GamePlayer(player, game, LocalDateTime.now());
            gamePlayerRepository.save(gamePlayer);
            responseEntity = new ResponseEntity<>(makeMap("gamePlayer_id", gamePlayer.getId()), HttpStatus.CREATED);
        }
        return responseEntity;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> game_viewDTO(GamePlayer gamePlayer, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player", authentication.getName());
        dto.put("view_id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(this::ShipDTO));
        dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(sgp -> sgp.getSalvoes().stream().map(this::salvoDTO)));
        return dto;
    }

    private Map<String, Object> salvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turnNumber", salvo.getTurnNumber());
        dto.put("location", salvo.getLocations());
        dto.put("hits", salvo.getHits());
        dto.put("sunk", salvo.getSunk());
        return dto;
    }

    private Map<String, Object> gamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("game_id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gamePlayers", game.getGamePlayers().stream().map(this::gamePlayersDTO).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> gamePlayersDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayer_id", gamePlayer.getId());
        dto.put("player", this.playersDTO(gamePlayer.getPlayer()));
        if (gamePlayer.getPlayer().getScore(gamePlayer.getGame()) != null)
            dto.put("score", gamePlayer.getPlayer().getScore(gamePlayer.getGame()).getScore());
        else
            dto.put("score", null);
        return dto;
    }

    private Map<String, Object> playersDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("player_id", player.getId());
        dto.put("email", player.geteMail());
        return dto;
    }

    public Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("locations", ship.getLocations());
        dto.put("type", ship.getShipType());
        return dto;
    }

    public Map<String, Object> playerStatisticsDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.geteMail());
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

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}