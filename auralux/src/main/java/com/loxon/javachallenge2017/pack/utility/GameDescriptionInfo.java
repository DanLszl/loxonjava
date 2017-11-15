package com.loxon.javachallenge2017.pack.utility;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GameDescriptionInfo {
    private static String ourUsername = "Preall";

    public static Player getOurPlayer(GameDescription gameDescription) {
        return gameDescription.getPlayers().stream()
                .filter(player -> ourUsername.equals(player.getUserName()))
                .findFirst()
                .get();
    }

    public static List<Player> getEnemyPlayers(GameDescription gameDescription) {
        return gameDescription.getPlayers().stream()
                .filter(player -> !ourUsername.equals(player.getUserName()))
                .collect(Collectors.toList());
    }

    public static Planet getPlanetWithId(GameDescription gameDescription, Integer id) {
        return gameDescription.getPlanets().stream()
                .filter(planet -> planet.getPlanetID().equals(id))
                .findFirst()
                .get();
    }

    public static boolean isOurPlayer(String id) {
        return getOurPlayer().getUserID().equals(id);
    }
}
