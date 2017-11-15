package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.features.DefaultFeature;
import com.loxon.javachallenge2017.features.Feature;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;

import java.util.*;

public class DefaultStrategy extends Strategy {

    public DefaultStrategy(GameDescription gameDescription) {
        super(gameDescription);
    }

    @Override
    public List<Response> getResponse(GameState gameState) {
        Feature feature = new DefaultFeature(gameDescription, gameState);

        Map<Integer, Double> values = feature.calculate();
        Integer oneOfTheBiggest = values.entrySet().stream()
                         .max(Map.Entry.comparingByValue())
                         .get()
                         .getKey();

        // Getting the biggest army:
        String us = "preall";
        PlanetState armysPlanet = gameState.getPlanetStates().stream()
                .filter(planetState -> us.equals(planetState.getOwner()))
                .max(Comparator.comparingDouble(
                        p ->
                        {
                            Optional<StationedArmy> army = ((PlanetState)p).getStationedArmies().stream()
                                    .filter(stationedArmy -> us.equals(stationedArmy.getOwner()))
                                    .findFirst();
                            if (army.isPresent()) {
                                return army.get().getSize();
                            } else {
                                return 0;
                            }
                        })
                ).get();

        StationedArmy ourArmy = armysPlanet.getStationedArmies().stream()
                .filter(army -> us.equals(army.getOwner()))
                .findFirst()
                .get();



        Response response = new Response(armysPlanet.getPlanetID(), oneOfTheBiggest, ourArmy.getSize());
        return Arrays.asList(response);
    }
}
