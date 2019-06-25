package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime gamePlayerDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(Player player, Game game, LocalDateTime gamePlayerDate) {
        this.setPlayer(player);
        this.setGame(game);
        this.setGamePlayerDate(gamePlayerDate);
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        getSalvoes().add(salvo);
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        getShips().add(ship);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getGamePlayerDate() {
        return gamePlayerDate;
    }

    private void setGamePlayerDate(LocalDateTime gamePlayerDate) {
        this.gamePlayerDate = gamePlayerDate;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    private Score getScore() {
        return getScore();
    }
}