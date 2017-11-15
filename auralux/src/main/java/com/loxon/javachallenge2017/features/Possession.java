package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;

import java.util.HashMap;
import java.util.Map;

public class Possession extends Feature {

    public Possession(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        Map<Integer, Double> map = new HashMap<>();
        for(PlanetState planetState : gameState.getPlanetStates()) {
            boolean areWeTheOwners = GameDescriptionInfo.isOurPlayer(planetState.getOwner());
            double functionValue = getFunctionValue(areWeTheOwners, planetState.getOwnershipRatio());
            map.put(planetState.getPlanetID(), functionValue);
        }
        return map;
    }

    private double getFunctionValue(boolean isOurPlayer, double possessionRatio) {
        return getPossessionFunctionValue(isOurPlayer ? -possessionRatio : possessionRatio);
    }

    private double getPossessionFunctionValue(double x) {
        if(x < -1.0 || x > 1.0) throw new RuntimeException("x value should be between -1 and 1!");
        double m = -4.0;
        double b = 5.0;
        if(x == -1.0 ) return 0.0;
        else return m * x + b;
    }
}
