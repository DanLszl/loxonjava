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
import java.util.function.Function;
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

        Map<Integer, Double> momentaryMagicNumbers = enemyAttackValues.entrySet().stream()
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

        // TODO
        double interpolationSmoothness = 0.2;

        magicNumbers = momentaryMagicNumbers.entrySet().stream()
                .collect(
                    Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> {
                                double previous = magicNumbers.get(entry.getKey());
                                double current = entry.getValue();
                                return (current - previous) * interpolationSmoothness + previous;
                            }
                    )
                );

        // Küldjünk oda, ahol nagyobb a magic number
        // De csak akkor, ha egy threshold-nál nagyobb a különbség
        Player ourPlayer = GameDescriptionInfo.getOurPlayer(gameDescription);

        Map<Integer, Double> ourStationedArmies
                = GameStateInfo.getStationedArmiesOfPlayer(gameState, ourPlayer.getUserID());



        // Vegyük azokat a bolygókat, ahonnan tudunk küldeni valakit

        int minMovableArmySize = gameDescription.getMinMovableArmySize();

        Map<Integer, Double> ourMovableArmies = ourStationedArmies.entrySet().stream()
                .filter(entry -> entry.getValue() > minMovableArmySize)
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue()
                        )
                );

        // nézzük végig ezeknek a bolygóknak a szomszédjait,
        // és minden bolygóhoz tároljuk el, hogy a szomszédai milyen helyzetben vannak

        Map<Integer, Map<Integer, Double>> helpablePlanets = ourMovableArmies.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> {
                                    Integer planetId = entry.getKey();
                                    Double movableArmySize = entry.getValue();
                                    // itt csinálunk egy map-et,
                                    // ami a szomszéd bolygókat mappeli a hozzájuk tartozó magicNumber-hez
                                    Planet planet = GameDescriptionInfo.getPlanetWithId(gameDescription, planetId);
                                    List<Integer> neighbors = planet.getNeighbours();

                                    double neighborMagicSum = neighbors.stream()
                                            .mapToDouble(neighbor -> magicNumbers.get(neighbor))
                                            .sum();

                                    double magicSum = neighborMagicSum + magicNumbers.get(entry.getKey());

                                    // Vegyük az arányokat ehhez a bolygóhoz tartozó magic number-ök,
                                    // és a szomszéd magic numberök között

                                    return neighbors.stream()
                                            .collect(
                                                    Collectors.toMap(
                                                            neighbor -> neighbor,
                                                            neighbor -> magicNumbers.get(neighbor) / magicSum
                                                    )
                                            );
                                }
                        )
                );

        // Ki kéne szedni a legrosszabb helyzetben lévő planet-et

        Map.Entry<Integer, Map<Integer, Double>> thisPlanetMustHelp = helpablePlanets.entrySet().stream()
                .max(
                        Comparator.comparingDouble(
                                entry ->
                                        entry.getValue().entrySet().stream()
                                                .mapToDouble(neighbor -> neighbor.getValue())
                                                .max()
                                                .orElse(0)
                        )
                )
                .orElse(null);

        Map.Entry<Integer, Double> thisPlanetNeedsTheMostHelp = thisPlanetMustHelp.getValue().entrySet().stream()
                .max(
                        Comparator.comparingDouble(
                                entry -> entry.getValue()
                        )
                )
                .orElse(null);


        Integer from = thisPlanetMustHelp.getKey();
        Integer to = thisPlanetNeedsTheMostHelp.getKey();

        Double size = (ourStationedArmies.get(from) * thisPlanetNeedsTheMostHelp.getValue());
        Integer sizeInt = size.intValue();

        Response response = new Response(from, to, sizeInt);


/*        for (Map.Entry<Integer, Double> stationedArmy : ourStationedArmies.entrySet()) {
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
        }*/



        List<MagicValue> magicValues = magicNumbers.entrySet().stream()
                .map(entry -> new MagicValue(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        MagicData magicData = new MagicData(magicValues);
        Gson gson = new Gson();
        System.out.println(gson.toJson(magicData));

        return response;
    }
}
