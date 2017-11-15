package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.Map;
import java.util.stream.Collectors;

public class EnemyAttackConcentration extends Feature {

    // TODO Being able to modify this
    private double discountFactor;

    public EnemyAttackConcentration(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        String ourPlayerId = GameDescriptionInfo.getOurPlayer(gameDescription).getUserID();
        Map<Integer, Double> enemyArmiesStrength = GameStateInfo.getArmiesStrengthOfEnemy(gameDescription, gameState, ourPlayerId, discountFactor);

        double enemyArmiesStrengthTogether = enemyArmiesStrength.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .sum();

        Map<Integer, Double> attackConcentration = enemyArmiesStrength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> 1.0 - (entry.getValue() / enemyArmiesStrengthTogether)
                        )
                );

        return Util.normalizeValues(attackConcentration);

    }
}
