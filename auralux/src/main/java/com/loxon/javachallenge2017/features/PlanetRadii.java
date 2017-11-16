package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.Map;
import java.util.stream.Collectors;

public class PlanetRadii extends Feature {
    public PlanetRadii(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        Map<Integer, Double> planetRadii = gameDescription.getPlanets().stream()
                .collect(
                        Collectors.toMap(
                                Planet::getPlanetID,
                                planet -> planet.getRadius().doubleValue()
                        )
                );
        return Util.normalizeRadiuses(planetRadii);
    }
}
