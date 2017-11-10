
package com.loxon.javachallenge2017.pack.stateclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GameState {

    @SerializedName("timeElapsed")
    @Expose
    private Integer timeElapsed;
    @SerializedName("planetStates")
    @Expose
    private List<PlanetState> planetStates = null;
    @SerializedName("remainingPlayers")
    @Expose
    private Integer remainingPlayers;
    @SerializedName("gameStatus")
    @Expose
    private String gameStatus;
    @SerializedName("standings")
    @Expose
    private List<Standing> standings = null;

    public Integer getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(Integer timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public List<PlanetState> getPlanetStates() {
        return planetStates;
    }

    public void setPlanetStates(List<PlanetState> planetStates) {
        this.planetStates = planetStates;
    }

    public Integer getRemainingPlayers() {
        return remainingPlayers;
    }

    public void setRemainingPlayers(Integer remainingPlayers) {
        this.remainingPlayers = remainingPlayers;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Standing> getStandings() {
        return standings;
    }

    public void setStandings(List<Standing> standings) {
        this.standings = standings;
    }

}
