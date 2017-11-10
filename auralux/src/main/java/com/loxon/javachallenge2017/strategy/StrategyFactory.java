package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;

public class StrategyFactory {
    public static Strategy getStrategy(GameDescription gameDescription) {
        return new DefaultStrategy(gameDescription);
    }
}
