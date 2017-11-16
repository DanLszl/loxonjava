package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.Map;
import java.util.stream.Collectors;

public class OurAttackConcentration extends Feature {

    private double discountFactor = 1.1;

    public OurAttackConcentration(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        String otherPlayer = GameDescriptionInfo.getEnemyPlayers(gameDescription).get(0).getUserID();
        Map<Integer, Double> ourArmiesStength = GameStateInfo.getArmiesStrengthOfEnemy(gameDescription, gameState, otherPlayer, discountFactor);

        double ourArmiesStrengthTogether = ourArmiesStength.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .sum();

        Map<Integer, Double> attackConcentration = ourArmiesStength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue() / ourArmiesStrengthTogether
                        )
                );

        return Util.normalizeValues(attackConcentration);
    }
}
