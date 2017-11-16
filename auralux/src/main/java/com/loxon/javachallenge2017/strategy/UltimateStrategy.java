package com.loxon.javachallenge2017.strategy;

import com.google.gson.Gson;
import com.loxon.javachallenge2017.features.*;
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

    Deque<GameState> gameStatesHistory = new ArrayDeque<>();
    public final int past = 3;

    Map<Integer, Double> magicNumbers;
    private double discountFactor = 1.05;

    public UltimateStrategy(GameDescription gameDescription) {
        super(gameDescription);
        magicNumbers = new HashMap<>();
        gameDescription.getPlanets().stream()
                .forEach(planet -> magicNumbers.put(planet.getPlanetID(), 0.0));
    }

    private void saveGameState(GameState gameState) {
        gameStatesHistory.add(gameState);
    }

    private GameState getPastGameState() {
        if (gameStatesHistory.size() > past) {
            GameState first = gameStatesHistory.getFirst();
            gameStatesHistory.removeFirst();
            return first;
        } else {
            return null;
        }
    }

    public Map<Integer, Double> calculateMagicNumbers(GameState gameState) {

        EnemyAttackConcentration enemyAttackConcentration = new EnemyAttackConcentration(gameDescription, gameState);
        PlanetArmiesStrength planetArmiesStrength = new PlanetArmiesStrength(gameDescription, gameState);
        PlanetRadii planetRadii = new PlanetRadii(gameDescription, gameState);
        Possession possession = new Possession(gameDescription, gameState);
        OurAttackConcentration ourAttackConcentration = new OurAttackConcentration(gameDescription, gameState);

        double w_ac = 0.05;
        double w_as = 0.05;
        double w_r = 0.02;
        double w_p = 1.0;
        double our_conc = 0.02;

        Map<Integer, Double> enemyAttackValues = enemyAttackConcentration.calculate();
        Map<Integer, Double> planetArmiesValues = planetArmiesStrength.calculate();
        Map<Integer, Double> planetRadiiValues = planetRadii.calculate();
        Map<Integer, Double> possessionValues = possession.calculate();
        Map<Integer, Double> ourAttackConcentrationValues = ourAttackConcentration.calculate();

        Map<Integer, Double> momentaryMagicNumbers = enemyAttackValues.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> {
                                    Integer id = entry.getKey();
                                    System.err.println(entry.getKey() + "ID: " +
                                            " Attack conc: " + w_ac * entry.getValue() +
                                            " Attack strent: " + w_as * planetArmiesValues.get(id) +
                                            " Radius: " + w_r * planetRadiiValues.get(id) +
                                            " Posssession: " + w_p * possessionValues.get(id) +
                                            " Ourconc: " + our_conc * ourAttackConcentrationValues.get(id));
                                    return w_ac * entry.getValue()
                                            - w_as * planetArmiesValues.get(id)
                                            + w_r * planetRadiiValues.get(id)
                                            + w_p * possessionValues.get(id)
                                            - our_conc * ourAttackConcentrationValues.get(id);
                                }
                        )
                );

        // TODO
        double interpolationSmoothness = 0.2;

        return momentaryMagicNumbers.entrySet().stream()
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

    }

    @Override
    public Response getResponse(GameState inGameState) {
        saveGameState(inGameState);
        final GameState gameState = getPastGameState();
        if (gameState == null) {
            return null;
        }

        magicNumbers = calculateMagicNumbers(gameState);

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
                .filter(integerDoubleEntry -> GameStateInfo.getPlanetsAndOwnershipOfPlayer(gameState, ourPlayer)
                        .get(integerDoubleEntry.getKey()) >= 1.0)
                .filter(integerDoubleEntry ->
                        GameStateInfo.getArmiesStrengthOfEnemy(gameDescription, gameState, ourPlayer.getUserID(), 1.1)
                                .get(integerDoubleEntry.getKey()) <
                                GameStateInfo.getArmiesStrengthOfPlayer(
                                        gameDescription, gameState, ourPlayer.getUserID(), 1.1)
                                        .get(integerDoubleEntry.getKey())
                )
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


        if (helpablePlanets.size() == 0) {
            // nem tudunk mit csinálni :(
            return null;
        }


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
//        Response response = null;


//        if(magicNumbers.get(from) * 1.2 < magicNumbers.get(to)) {
//            //System.err.println(from + "  ->>>>>  " + to);
//
//            Double numOfAllSoldiers = ourStationedArmies.get(from);
//            Double size = (ourStationedArmies.get(from) * thisPlanetNeedsTheMostHelp.getValue());
//            Integer sizeInt = size.intValue();
//
//            if (numOfAllSoldiers > gameDescription.getMinMovableArmySize())
//                sizeInt = Math.max(sizeInt, gameDescription.getMinMovableArmySize());
//
//            if (sizeInt >= gameDescription.getMinMovableArmySize()) response = new Response(from, to, sizeInt);
//        }
        Double numOfAllSoldiers = ourStationedArmies.get(from);

        Map<Integer, Double> armiesStrength = GameStateInfo.getArmiesStrengthOfPlayer(gameDescription, gameState, ourPlayer.getUserID(), discountFactor);
        Double fromArmiesStrength = armiesStrength.get(from);
        Double toArmiesStrength = armiesStrength.get(to);

//        (a-x)/(b+x) = q
//        a-x = qb + qx
//        x = a-qb-qx
//        (1+q)x = a-qb
//        x = (a-qb)/(1+q)

        double q = thisPlanetNeedsTheMostHelp.getValue();
        double armySize = (fromArmiesStrength - q * toArmiesStrength)/(1 + q);


        // Double size = (ourStationedArmies.get(from) * thisPlanetNeedsTheMostHelp.getValue());
        Double size = armySize;
        Integer sizeInt = size.intValue();

        if (numOfAllSoldiers > gameDescription.getMinMovableArmySize())
            sizeInt = Math.max(sizeInt, gameDescription.getMinMovableArmySize());

        Response response = null;
        if(sizeInt >= gameDescription.getMinMovableArmySize())
            response = new Response(from, to, sizeInt);

        // Magic number printing
        List<MagicValue> magicValues = magicNumbers.entrySet().stream()
                .map(entry -> new MagicValue(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        MagicData magicData = new MagicData(magicValues);
        Gson gson = new Gson();
        System.out.println(gson.toJson(magicData));

        return response;
    }
}
