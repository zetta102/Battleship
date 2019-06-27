package com.codeoftheweb.salvo;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private ShipType shipType;

    @ElementCollection
    @Column(name = "location")
    private List<String> locations = new ArrayList<>();

    public Ship() {
    }

    public Ship(ShipType shipType, List<String> shipLocation) {
        this.setLocations(shipLocation);
        this.setShipType(shipType);
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setList(List<String> locations) {
        this.setLocations(locations);
    }
}