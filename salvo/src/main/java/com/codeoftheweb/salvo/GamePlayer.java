package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final
    Set<Ship> ships = new HashSet<>();
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private final
    Set<Salvo> salvoes = new HashSet<>();
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

    public int getSunkCount() {
        int sunkCount = 0;
        Salvo lastSalvo = this.getSalvoes().stream().filter(salvo -> salvo.getTurnNumber() == this.getSalvoes().stream().mapToInt(Salvo::getTurnNumber).max().orElse(1)).findFirst().orElse(null);
        if (lastSalvo != null) {
            sunkCount = lastSalvo.getSunkCount().size();
        }
        return sunkCount;
    }

    public GameState gameState() {
        GameState gameState;
        GamePlayer opponent = this.getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getId()).findFirst().orElse(null);
        if (this.getShips().size() == 0) {
            gameState = GameState.SET_SHIPS;
            return gameState;
        } else {
            if (opponent == null) {
                gameState = GameState.WAITING_OPPONENT;
                return gameState;
            } else {
                if (opponent.getShips().size() == 0) {
                    gameState = GameState.WAITING_OPPONENT_SHIPS;
                    return gameState;
                } else {
                    int myTurn = this.getSalvoes().stream().mapToInt(Salvo::getTurnNumber).max().orElse(0) + 1;
                    int opponentTurn = opponent.getSalvoes().stream().mapToInt(Salvo::getTurnNumber).max().orElse(0) + 1;
                    if (myTurn > opponentTurn) {
                        gameState = GameState.WAITING_OPPONENT_SALVOES;
                        return gameState;
                    } else {
                        if (opponentTurn > myTurn) {
                            gameState = GameState.SHOOTING_SALVOES;
                            return gameState;
                        } else {
                            if (((this.getSunkCount().size() == 5) && (this.getSunkCount().size() == opponent.getSunkCount().size()))) {
                                gameState = GameState.YOU_TIED;
                                return gameState;
                            } else {
                                if ((this.getSunkCount() == 5) && (this.getSunkCount() > opponent.getSunkCount())) {
                                    gameState = GameState.YOU_LOST;
                                    return gameState;
                                } else {
                                    if ((opponent.getSunkCount() == 5) && (opponent.getSunkCount() > this.getSunkCount())) {
                                        gameState = GameState.YOU_WON;
                                        return gameState;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return gameState;
    }
}