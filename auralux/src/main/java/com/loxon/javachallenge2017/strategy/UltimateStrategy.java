package com.loxon.javachallenge2017.strategy;

import com.google.gson.Gson;
import com.loxon.javachallenge2017.features.EnemyAttackConcentration;
import com.loxon.javachallenge2017.features.PlanetArmiesStrength;
import com.loxon.javachallenge2017.features.PlanetRadii;
import com.loxon.javachallenge2017.features.Possession;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.magicnumber.MagicData;
import com.loxon.javachallenge2017.pack.magicnumber.MagicValue;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;

import java.util.*;
import java.util.stream.Collectors;

public class UltimateStrategy extends Strategy {

    Map<Integer, Double> magicNumbers;

    public UltimateStrategy(GameDescription gameDescription) {
        super(gameDescription);
        magicNumbers = new HashMap<>();
        gameDescription.getPlanets().stream()
                .forEach(planet -> magicNumbers.put(planet.getPlanetID(), 0.0));
    }



    @Override
    public Response getResponse(GameState gameState) {
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

        Map<Integer, Double> tempMagicNumbers = enemyAttackValues.entrySet().stream()
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

        magicNumbers.entrySet().stream()
                .forEach(entry -> {
                    double previous = entry.getValue();
                    double current = tempMagicNumbers.get(entry.getKey());
                    double nextValue = (current-previous) * 0.1 + previous;
                    magicNumbers.put(entry.getKey(), nextValue);
                });


        // Küldjünk oda, ahol nagyobb a magic number
        // De csak akkor, ha egy threshold-nál nagyobb a különbség

        //GameStateInfo.getPlanetsWithStationedArmies()
        Player ourPlayer = GameDescriptionInfo.getOurPlayer(gameDescription);

        Map<Integer, Double> ourStationedArmies = GameStateInfo.getStationedArmiesOfPlayer(gameState, ourPlayer.getUserID());

        // nézzük végig a bolygók szomszédjait

        List<Response> responses = new ArrayList<>();

        for (Map.Entry<Integer, Double> stationedArmy : ourStationedArmies.entrySet()) {
            Planet planet = GameDescriptionInfo.getPlanetWithId(gameDescription, stationedArmy.getKey());
            Double maxMagic = -600.0;
            Integer maxId = 0;
            for (Integer neighborId : planet.getNeighbours()) {
                double currentMagic = magicNumbers.get(neighborId);
                if (currentMagic > maxMagic) {
                    maxMagic = currentMagic;
                    maxId = neighborId;
                }
            }

            if (magicNumbers.get(maxId) > magicNumbers.get(stationedArmy.getKey()) * 1.4) {
                responses.add(new Response(stationedArmy.getKey(), maxId, stationedArmy.getValue().intValue()));
            }
        }

//        ourStationedArmies.entrySet().stream()
//                .forEach(
//                        entry -> {
//                            Planet planet = GameDescriptionInfo.getPlanetWithId(gameDescription, entry.getKey());
//                            Integer worstNeighbor = planet.getNeighbours().stream().max(
//                                    Comparator.comparingDouble(magicNumbers::get)
//                            ).orElseGet(null);
//                            if (magicNumbers.get(worstNeighbor) > magicNumbers.get(entry.getKey()) * 1.05) {
//                                responses.add(new Response(entry.getKey(), worstNeighbor, (int) (entry.getValue() * 0.8)));
//                            }
//                        }
//                );
//
        try {

            List<MagicValue> magicValues = magicNumbers.entrySet().stream()
                    .map(entry -> new MagicValue(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            MagicData magicData = new MagicData();
            magicData.setMagicValues(magicValues);
            Gson gson = new Gson();
            System.out.println(gson.toJson(magicData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return responses;
        // TODO
        return null;
    }
}
