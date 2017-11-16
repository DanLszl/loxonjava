package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;

public class StrategyFactory {
    public static Strategy getStrategy(GameDescription gameDescription) {
        // TODO
        return new UltimateStrategy(gameDescription);
        //return new DefaultStrategy(gameDescription);
    }
}
