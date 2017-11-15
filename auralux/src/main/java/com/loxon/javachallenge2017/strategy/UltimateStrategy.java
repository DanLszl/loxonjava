package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.features.EnemyAttackConcentration;
import com.loxon.javachallenge2017.features.PlanetArmiesStrength;
import com.loxon.javachallenge2017.features.PlanetRadii;
import com.loxon.javachallenge2017.features.Possession;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;

import java.util.*;
import java.util.stream.Collectors;

public class UltimateStrategy extends Strategy {
    public UltimateStrategy(GameDescription gameDescription) {
        super(gameDescription);
    }

    @Override
    public List<Response> getResponse(GameState gameState) {
        EnemyAttackConcentration enemyAttackConcentration = new EnemyAttackConcentration(gameDescription, gameState);
        PlanetArmiesStrength planetArmiesStrength = new PlanetArmiesStrength(gameDescription, gameState);
        PlanetRadii planetRadii = new PlanetRadii(gameDescription, gameState);
        Possession possession = new Possession(gameDescription, gameState);

        double w_ac = 1.0;
        double w_as = 1.0;
        double w_r = 1.0;
        double w_p = 1.0;

        Map<Integer, Double> enemyAttackValues = enemyAttackConcentration.calculate();
        Map<Integer, Double> planetArmiesValues = planetArmiesStrength.calculate();
        Map<Integer, Double> planetRadiiValues = planetRadii.calculate();
        Map<Integer, Double> possessionValues = possession.calculate();

        Map<Integer, Double> magicNumbers = enemyAttackValues.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> {
                                    Integer id = entry.getKey();
                                    return w_ac * entry.getValue()
                                            + w_as * planetArmiesValues.get(id)
                                            + w_r * planetRadiiValues.get(id)
                                            + w_p * possessionValues.get(id);
                                }
                        )
                );



        // Küldjünk oda, ahol nagyobb a magic number
        // De csak akkor, ha egy threshold-nál nagyobb a különbség

        //GameStateInfo.getPlanetsWithStationedArmies()
        Player ourPlayer = GameDescriptionInfo.getOurPlayer(gameDescription);

        Map<Integer, Double> ourStationedArmies = GameStateInfo.getStationedArmiesOfPlayer(gameState, ourPlayer.getUserID());

        // nézzük végig a bolygók szomszédjait

        List<Response> responses = new ArrayList<>();

        ourStationedArmies.entrySet().stream()
                .forEach(
                        entry -> {
                            Planet planet = GameDescriptionInfo.getPlanetWithId(gameDescription, entry.getKey());
                            Integer worstNeighbor = planet.getNeighbours().stream().max(
                                    Comparator.comparingDouble(magicNumbers::get)
                            ).orElseGet(null);
                            if (magicNumbers.get(worstNeighbor) > magicNumbers.get(entry.getKey()) * 1.05) {
                                responses.add(new Response(entry.getKey(), worstNeighbor, (int) (entry.getValue() * 0.8)));
                            }
                        }
                );

        return responses;
    }
}
