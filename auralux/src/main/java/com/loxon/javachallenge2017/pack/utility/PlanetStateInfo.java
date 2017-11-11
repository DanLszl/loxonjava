package com.loxon.javachallenge2017.pack.utility;

import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;

public class PlanetStateInfo {
    public static StationedArmy getStationedArmyOfPlayer(PlanetState planetState, Player player) {
        return planetState.getStationedArmies().stream()
                .filter(stationedArmy -> player.getUserID().equals(stationedArmy.getOwner()))
                .findFirst()
                .orElse(null);
    }

    public static StationedArmy getStationedArmyOfEnemy(PlanetState planetState, Player player) {
        return planetState.getStationedArmies().stream()
                .filter(stationedArmy -> !player.getUserID().equals(stationedArmy.getOwner()))
                .findFirst()
                .orElse(null);
    }
}
