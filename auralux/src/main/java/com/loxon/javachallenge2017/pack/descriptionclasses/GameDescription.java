
package com.loxon.javachallenge2017.pack.descriptionclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameDescription {

    @SerializedName("mapSizeY")
    @Expose
    private Integer mapSizeY;
    @SerializedName("unitCreateSpeed")
    @Expose
    private Double unitCreateSpeed;
    @SerializedName("battleSpeed")
    @Expose
    private Integer battleSpeed;
    @SerializedName("mapSizeX")
    @Expose
    private Integer mapSizeX;
    @SerializedName("players")
    @Expose
    private List<Player> players = null;
    @SerializedName("commandSchedule")
    @Expose
    private Integer commandSchedule;
    @SerializedName("broadcastSchedule")
    @Expose
    private Integer broadcastSchedule;
    @SerializedName("movementSpeed")
    @Expose
    private Integer movementSpeed;
    @SerializedName("captureSpeed")
    @Expose
    private Integer captureSpeed;
    @SerializedName("planetExponent")
    @Expose
    private Double planetExponent;
    @SerializedName("planets")
    @Expose
    private List<Planet> planets = null;
    @SerializedName("internalSchedule")
    @Expose
    private Integer internalSchedule;
    @SerializedName("battleExponent")
    @Expose
    private Double battleExponent;
    @SerializedName("minMovableArmySize")
    @Expose
    private Integer minMovableArmySize;
    @SerializedName("gameLength")
    @Expose
    private Integer gameLength;

    public Integer getMapSizeY() {
        return mapSizeY;
    }

    public void setMapSizeY(Integer mapSizeY) {
        this.mapSizeY = mapSizeY;
    }

    public Double getUnitCreateSpeed() {
        return unitCreateSpeed;
    }

    public void setUnitCreateSpeed(Double unitCreateSpeed) {
        this.unitCreateSpeed = unitCreateSpeed;
    }

    public Integer getBattleSpeed() {
        return battleSpeed;
    }

    public void setBattleSpeed(Integer battleSpeed) {
        this.battleSpeed = battleSpeed;
    }

    public Integer getMapSizeX() {
        return mapSizeX;
    }

    public void setMapSizeX(Integer mapSizeX) {
        this.mapSizeX = mapSizeX;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Integer getCommandSchedule() {
        return commandSchedule;
    }

    public void setCommandSchedule(Integer commandSchedule) {
        this.commandSchedule = commandSchedule;
    }

    public Integer getBroadcastSchedule() {
        return broadcastSchedule;
    }

    public void setBroadcastSchedule(Integer broadcastSchedule) {
        this.broadcastSchedule = broadcastSchedule;
    }

    public Integer getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(Integer movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public Integer getCaptureSpeed() {
        return captureSpeed;
    }

    public void setCaptureSpeed(Integer captureSpeed) {
        this.captureSpeed = captureSpeed;
    }

    public Double getPlanetExponent() {
        return planetExponent;
    }

    public void setPlanetExponent(Double planetExponent) {
        this.planetExponent = planetExponent;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public Integer getInternalSchedule() {
        return internalSchedule;
    }

    public void setInternalSchedule(Integer internalSchedule) {
        this.internalSchedule = internalSchedule;
    }

    public Double getBattleExponent() {
        return battleExponent;
    }

    public void setBattleExponent(Double battleExponent) {
        this.battleExponent = battleExponent;
    }

    public Integer getMinMovableArmySize() {
        return minMovableArmySize;
    }

    public void setMinMovableArmySize(Integer minMovableArmySize) {
        this.minMovableArmySize = minMovableArmySize;
    }

    public Integer getGameLength() {
        return gameLength;
    }

    public void setGameLength(Integer gameLength) {
        this.gameLength = gameLength;
    }

}
