package com.loxon.javachallenge2017.features;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.MovingArmy;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;

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

        // Getting stationed armies
        Map<Integer, List<StationedArmy>> planetStationedArmiesMap = GameStateInfo.getStationedArmies(gameState);


        // Getting moving armies
        Map<Integer, List<MovingArmy>> planetMovingArmiesMap = GameStateInfo.getMovingArmies(gameState);

        // Our stationed armies
        Map<Integer, Double> ourStationedArmy = GameStateInfo.getStationedArmiesOfPlayer(gameState, ourPlayerId);

        // Enemy stationed army
        Map<Integer, Double> enemyStationedArmy = GameStateInfo.getEnemyStationedArmies(gameState, ourPlayerId);

        // Our moving armies
        Map<Integer, List<MovingArmy>> ourMovingArmies = GameStateInfo.getMovingArmiesOfPlayer(gameState, ourPlayerId);

        // Enemy moving armies
        Map<Integer, List<MovingArmy>> enemyMovingArmies = GameStateInfo.getMovingArmiesOfEnemies(gameState, ourPlayerId);

        // Our discounted armies
        Map<Integer, Double> ourDiscountedMovingArmies = GameStateInfo.getDiscountedMovingArmiesOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor)

        // Enemy discounted armies
        Map<Integer, Double> enemyDiscountedMovingArmies = GameStateInfo.getDiscountedMovingArmiesOfEnemy(gameDescription, gameState, ourPlayerId, discountFactor);

        Map<Integer, Double> ourArmiesStrength = GameStateInfo.getArmiesStrengthOfPlayer(gameDescription, gameState, ourPlayerId, discountFactor);ourStationedArmy.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() + ourDiscountedMovingArmies.get(entry.getKey())
                        )
                );

        Map<Integer, Double> enemyArmiesStrength = enemyStationedArmy.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() + enemyDiscountedMovingArmies.get(entry.getKey())
                        )
                );

        Map<Integer, Double> planetArmiesStrength = enemyArmiesStrength.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() / ourArmiesStrength.get(entry.getKey())

                        )
                );

        return planetArmiesStrength;
    }
}
