package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;

import java.util.Map;

public abstract class Feature {
    protected GameDescription gameDescription;
    protected GameState gameState;

    public Feature(GameDescription gameDescription, GameState gameState) {
        this.gameDescription = gameDescription;
        this.gameState = gameState;
    }

    public abstract Map<Integer, Double> calculate();
}
