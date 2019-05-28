package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @OneToMany(mappedBy = "score", fetch = FetchType.EAGER)
    private long id;
    private LocalDateTime creationDate;

    public Game() { }

    public Game(LocalDateTime creationDate) {
        this.setCreationDate(creationDate);
    }

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private
    Set<GamePlayer> gamePlayers;

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

    public Game(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
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
}
