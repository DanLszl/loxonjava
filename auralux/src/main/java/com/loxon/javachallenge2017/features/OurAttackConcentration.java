package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.Map;
import java.util.stream.Collectors;

public class OurAttackConcentration extends Feature {

    private double discountFactor = 1.2;

    public OurAttackConcentration(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        String ourPlayerId = GameDescriptionInfo.getOurPlayer(gameDescription).getUserID();
        Map<Integer, Double> ourArmiesStength = GameStateInfo.getArmiesStrengthOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor);

        double ourArmiesStrengthTogether = ourArmiesStength.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .sum();

        double maxOurArmiesStrengthTogether = Math.max(ourArmiesStrengthTogether, 0.01);

        Map<Integer, Double> attackConcentration = ourArmiesStength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue() / maxOurArmiesStrengthTogether
                        )
                );

        return attackConcentration;
    }
}
