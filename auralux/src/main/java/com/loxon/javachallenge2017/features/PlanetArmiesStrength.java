package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.Map;
import java.util.stream.Collectors;


public class PlanetArmiesStrength extends Feature {

    // TODO be able to change this from outside
    private double discountFactor = 1.1;


    public PlanetArmiesStrength(GameDescription gameDescription, GameState gameState) {
        super(gameDescription, gameState);
    }

    @Override
    public Map<Integer, Double> calculate() {
        Player ourPlayer = GameDescriptionInfo.getOurPlayer(gameDescription);
        String ourPlayerId = ourPlayer.getUserID();

        Map<Integer, Double> ourArmiesStrength = GameStateInfo.getArmiesStrengthOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor);
        Map<Integer, Double> enemyArmiesStrength = GameStateInfo.getArmiesStrengthOfEnemy(gameDescription, gameState, ourPlayerId, discountFactor);

        Map<Integer, Double> planetArmiesStrength = enemyArmiesStrength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() / 3.0 * (1 + ourArmiesStrength.get(entry.getKey()))

                        )
                );

        return Util.normalizeValues(planetArmiesStrength);
    }
}
