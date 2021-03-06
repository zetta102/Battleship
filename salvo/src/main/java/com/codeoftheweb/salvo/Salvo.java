package com.codeoftheweb.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turnNumber;


    @ElementCollection
    @Column(name = "location")
    private List<String> locations = new ArrayList<>();

    public Salvo(int turnNumber, List<String> salvoLocation) {
        setTurnNumber(turnNumber);
        setLocations(salvoLocation);
    }

    public Salvo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    private void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public List<String> getLocations() {
        return locations;
    }

    private void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getHits() {
        List<String> hits = new ArrayList<>();
        GamePlayer opponent = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getGamePlayer().getId()).findFirst().orElse(null);
        if (opponent != null) {
            hits = this.getLocations().stream().filter(loc -> opponent.getShips().stream().anyMatch(ship -> ship.getLocations().contains(loc))).collect(Collectors.toList());
        }
        return hits;
    }

    public List<Map<String, Object>> getSunk() {
        List<Map<String, Object>> sunk = new ArrayList<>();
        List<String> locs = new ArrayList<>();
        this.getGamePlayer().getSalvoes().stream().filter(salvo -> salvo.getTurnNumber() <= this.getTurnNumber()).forEach(salvo -> locs.addAll(salvo.getLocations()));
        GamePlayer opponent = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getGamePlayer().getId()).findFirst().orElse(null);
        if (opponent != null) {

            sunk = opponent.getShips().stream().filter(ship -> locs.containsAll(ship.getLocations())).map(Ship::ShipDTO).collect(Collectors.toList());
        }
        return sunk;
    }

    public List<Map<String, Object>> getSunkCount() {
        List<Map<String, Object>> sunkCount = new ArrayList<>();
        List<String> locs = new ArrayList<>();
        this.getGamePlayer().getSalvoes().stream().filter(salvo -> salvo.getTurnNumber() <= this.getTurnNumber()).forEach(salvo -> locs.addAll(salvo.getLocations()));
        GamePlayer opponent = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gp -> gp.getId() != this.getGamePlayer().getId()).findFirst().orElse(null);
        if (opponent != null) {

            sunkCount = opponent.getShips().stream().filter(ship -> locs.containsAll(ship.getLocations())).map(Ship::ShipDTO).collect(Collectors.toList());
        }
        return sunkCount;
    }
}