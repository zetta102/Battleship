package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private
    Set<GamePlayer> gamePlayers;


    private String eMail;
    private String password;

    public Player() {}

    public Player(String mail, String pass) {
        seteMail(mail);
        setPassword(pass);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String geteMail() {
        return eMail;
    }

    private void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        getGamePlayers().add(gamePlayer);
    }

    public List<Game> getGames() {
        return getGamePlayers().stream().map(GamePlayer::getGame).collect(toList());
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public Score getScore(Game game) {
        return getScores().stream().filter(s -> s.getGame().getId() == game.getId()).findFirst().orElse(null);
    }

    private Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }
}

