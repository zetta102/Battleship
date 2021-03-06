package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;


    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    private GameState GameState;

    public Game() {
    }

    public Game(LocalDateTime creationDate, GameState gameState) {
        this.setCreationDate(creationDate);
        this.setGameState(gameState);
    }

    public Game(LocalDateTime creationDate) {
        this.setCreationDate(creationDate);
    }

    public Game(GameState gameState) {
        this.setGameState(gameState);
    }

    public Game(Set<GamePlayer> gamePlayers) {
        this.setGamePlayers(gamePlayers);
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        getGamePlayers().add(gamePlayer);
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return getGamePlayers().stream().map(GamePlayer::getPlayer).collect(toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public GameState getGameState() {
        return GameState;
    }

    public void setGameState(GameState gameState) {
        GameState = gameState;
    }
}