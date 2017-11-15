package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.MovingArmy;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.Util;

import java.util.List;
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
/*
        Map<Integer, List<StationedArmy>> planetStationedArmiesMap = GameStateInfo.getStationedArmies(gameState);
        Map<Integer, List<MovingArmy>> planetMovingArmiesMap = GameStateInfo.getMovingArmies(gameState);
        Map<Integer, Double> ourStationedArmy = GameStateInfo.getStationedArmiesOfPlayer(gameState, ourPlayerId);
        Map<Integer, Double> enemyStationedArmy = GameStateInfo.getStationedArmiesOfEnemy(gameState, ourPlayerId);
        Map<Integer, List<MovingArmy>> ourMovingArmies = GameStateInfo.getMovingArmiesOfPlayer(gameState, ourPlayerId);
        Map<Integer, List<MovingArmy>> enemyMovingArmies = GameStateInfo.getMovingArmiesOfEnemies(gameState, ourPlayerId);
        Map<Integer, Double> ourDiscountedMovingArmies = GameStateInfo.getDiscountedMovingArmiesOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor)
        Map<Integer, Double> enemyDiscountedMovingArmies = GameStateInfo.getDiscountedMovingArmiesOfEnemy(gameDescription, gameState, ourPlayerId, discountFactor);
*/

        Map<Integer, Double> ourArmiesStrength = GameStateInfo.getArmiesStrengthOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor);
        Map<Integer, Double> enemyArmiesStrength = GameStateInfo.getArmiesStrengthOfEnemy(gameDescription, gameState, ourPlayerId, discountFactor);

        Map<Integer, Double> planetArmiesStrength = enemyArmiesStrength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() / (1 + ourArmiesStrength.get(entry.getKey()))

                        )
                );

        return Util.normalizeValues(planetArmiesStrength);
    }
}
