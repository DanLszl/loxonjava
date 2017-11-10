package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultFeature extends Feature {
    public DefaultFeature(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Planet, Double> calculate() {
        String us = "preall";
        return gameDescription.getPlanets().stream()
                                    .filter(planet ->
                                                !us.equals(gameState.getPlanetStates().stream()
                                                                           .filter(planetState -> planetState.getPlanetID() == planet.getPlanetID())
                                                                           .findFirst()
                                                                        .get().getOwner())
                                    )
                                    .collect(Collectors.toMap(Function.identity(), notOurPlanet -> (double) notOurPlanet.getRadius()));
    }
}
