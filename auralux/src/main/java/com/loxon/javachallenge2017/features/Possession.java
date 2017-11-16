package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

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
            boolean areWeTheOwners = GameDescriptionInfo.isOurPlayer(gameDescription, planetState.getOwner());
            double functionValue = getFunctionValue(areWeTheOwners, planetState.getOwnershipRatio());
            map.put(planetState.getPlanetID(), functionValue);
        }
        return Util.normalizeValues(map);
    }

    private double getFunctionValue(boolean isOurPlayer, double possessionRatio) {
        return getPossessionFunctionValue(isOurPlayer ? -possessionRatio : possessionRatio);
    }
//
//    private double getPossessionFunctionValue(double x) {
//        if(x < -1.0 || x > 1.0) throw new RuntimeException("x value should be between -1 and 1!");
//        double m = -4.0;
//        double b = 10.0;
//        if(x == -1.0 ) return 0.01;
//        else if (x == 0.0) return b*2;
//        else return m * x + b;
//    }

    private double getPossessionFunctionValue(double x) {
        if(x < -1.0 || x > 1.0) throw new RuntimeException("x value should be between -1 and 1!");
        double b = 10.0;
        if(x == -1.0 ) return 0.01;
        //else if (x == 0.0) return b*2;
            //else if (x < 0.0) return b;
        else if (x == 1.0)
            return 0.001;
        else
            return -Math.tan(x) + b;
    }
}
