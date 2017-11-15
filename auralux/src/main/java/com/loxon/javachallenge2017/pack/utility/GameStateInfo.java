package com.loxon.javachallenge2017.pack.utility;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.MovingArmy;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameStateInfo {
    public static Map<Integer, Double> getPlanetsAndOwnershipOfPlayer(GameState gameState, Player player) {
        return gameState.getPlanetStates().stream()
                .filter(planetState -> player.getUserID().equals(planetState.getOwner()))
                .collect(Collectors.toMap(
                        PlanetState::getPlanetID, PlanetState::getOwnershipRatio
                ));

    }


    // Itt lehet null a stationedArmy...
    public static Map<Integer, List<StationedArmy>> getStationedArmies(GameState gameState) {
        return gameState.getPlanetStates().stream()
                .collect(
                        Collectors.toMap(
                                PlanetState::getPlanetID,
                                PlanetState::getStationedArmies
                        )
                );

    }

    public static Map<Integer, List<MovingArmy>> getMovingArmies(GameState gameState) {
        return gameState.getPlanetStates().stream()
                .collect(
                        Collectors.toMap(
                                PlanetState::getPlanetID,
                                PlanetState::getMovingArmies
                        )
                );
    }

    public static Map<Integer, Double> getStationedArmiesOfPlayer(GameState gameState, String playerId) {
        Map<Integer, List<StationedArmy>> planetStationedArmiesMap = getStationedArmies(gameState);
        return planetStationedArmiesMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry != null
                                        ? entry.getValue().stream()
                                                .filter(stationedArmy -> playerId.equals(stationedArmy.getOwner()))
                                                .mapToDouble(stationedArmy -> stationedArmy.getSize().doubleValue())
                                                .sum()
                                        : 0
                        )
                );
    }

    public static Map<Integer, Double> getEnemyStationedArmies(GameState gameState, String playerId) {
        Map<Integer, List<StationedArmy>> planetStationedArmiesMap = getStationedArmies(gameState);
        return planetStationedArmiesMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry != null
                                        ? entry.getValue().stream()
                                                .filter(stationedArmy -> !playerId.equals(stationedArmy.getOwner()))
                                                .mapToDouble(stationedArmy -> stationedArmy.getSize().doubleValue())
                                                .sum()
                                        : 0
                        )
                );
    }

    public static Map<Integer, List<MovingArmy>> getMovingArmiesOfPlayer(GameState gameState, String playerId) {
        Map<Integer, List<MovingArmy>> planetMovingArmiesMap = getMovingArmies(gameState);
        return planetMovingArmiesMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue().stream()
                                        .filter(movingArmy -> playerId.equals(movingArmy.getOwner()))
                                        .collect(Collectors.toList())
                        )
                );
    }

    public static Map<Integer, List<MovingArmy>> getMovingArmiesOfEnemies(GameState gameState, String playerId) {
        Map<Integer, List<MovingArmy>> planetMovingArmiesMap = getMovingArmies(gameState);
        return planetMovingArmiesMap.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry.getValue().stream()
                                        .filter(movingArmy -> !playerId.equals(movingArmy.getOwner()))
                                        .collect(Collectors.toList())
                        )
                );
    }

    public static double distanceBetweenAB(double ax, double ay, double bx, double by) {
        return Math.sqrt((ax-bx)*(ax-bx) + (ay-by)*(ay-by));
    }


    public static double getMovingArmyTime(GameDescription gameDescription, Integer planetID, MovingArmy movingArmy) {
        double distance;
        Planet planet = GameDescriptionInfo.getPlanetWithId(gameDescription, planetID);
        distance = distanceBetweenAB(planet.getX(), planet.getY(), movingArmy.getX(), movingArmy.getY());
        return distance / gameDescription.getMovementSpeed();
    }

    public static Map<Integer, Double> getDiscountedMovingArmiesOfPlayer(GameDescription gameDescription, GameState gameState, String playerId, Double discountFactor) {
        Map<Integer, List<MovingArmy>> ourMovingArmies = getMovingArmiesOfPlayer(gameState, playerId);
        return ourMovingArmies.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry != null
                                        ?   entry.getValue().stream()
                                            .map(movingArmy -> {
                                                double time = getMovingArmyTime(gameDescription, entry.getKey(), movingArmy);
                                                return movingArmy.getSize() / Math.pow(discountFactor, time);
                                            })
                                            .mapToDouble(Double::doubleValue)
                                            .sum()
                                        : 0
                        )
                );
    }

    public static Map<Integer, Double> getDiscountedMovingArmiesOfEnemy(GameDescription gameDescription, GameState gameState, String playerId, Double discountFactor) {
        Map<Integer, List<MovingArmy>> enemyMovingArmies = getMovingArmiesOfEnemies(gameState, playerId);
        return enemyMovingArmies.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> entry != null
                                        ?   entry.getValue().stream()
                                            .map(movingArmy -> {
                                                double time = getMovingArmyTime(gameDescription, entry.getKey(), movingArmy);
                                                return movingArmy.getSize() / Math.pow(discountFactor, time);
                                            })
                                            .mapToDouble(Double::doubleValue)
                                            .sum()
                                        : 0
                        )
                );
    }

    public static Map<Integer, Double> getArmiesStrengthOfPlayer(GameDescription gameDescription, GameState gameState, String playerId, Double discountFactor) {
        Map<Integer, Double> ourStationedArmy = getStationedArmiesOfPlayer(gameState, playerId);
        Map<Integer, Double> ourDiscountedMovingArmies = getDiscountedMovingArmiesOfPlayer(gameDescription, gameState, playerId, discountFactor);

        return ourStationedArmy.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry ->
                                        entry.getValue() + ourDiscountedMovingArmies.get(entry.getKey())
                        )
                );
    }

    public static List<Player> getEnemyPlayers(GameDescription gameDescription, Player player) {
        return gameDescription.getPlayers().stream()
                .filter(enemy -> !enemy.getUserID().equals(player.getUserID()))
                .collect(Collectors.toList());
    }

    public static List<PlanetState> getEnemyPlanetStates(GameState gameState, Player player) {
        return gameState.getPlanetStates().stream()
                .filter(planetState -> !player.getUserID().equals(planetState.getOwner()))
                .collect(Collectors.toList());
    }

    public static Integer getBiggestEmptyOrWeakest(GameDescription gameDescription, GameState gameState, Player player) {
        Optional<PlanetState> biggestEmpty = gameState.getPlanetStates().stream()
                .filter(planetState -> planetState.getOwner() == null)
                .max(Comparator.comparingInt(planetState -> GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()).getRadius()));
        if (biggestEmpty.isPresent()) {
            return biggestEmpty.get().getPlanetID();
        } else {
            List<PlanetState> enemyPlanetStates = getEnemyPlanetStates(gameState, player);
            return enemyPlanetStates.stream()
                    .min(Comparator.comparingInt(
                            planetState -> planetState.getStationedArmies().stream().
                                    mapToInt(stationedArmy -> stationedArmy.getSize()).sum()))
                    .get()
                    .getPlanetID();
        }
    }

    public static List<PlanetState> getFullyOccupiedPlanetsWithStationedArmiesOfPlayer(GameState gameState, Player player) {
        return gameState.getPlanetStates().stream()
                .filter(planetState -> planetState.getOwnershipRatio().equals(1.0))
                .filter(planetState ->
                        planetState.getStationedArmies().stream()
                                .anyMatch(stationedArmy -> player.getUserID().equals(stationedArmy.getOwner())) )
                .collect(Collectors.toList());
    }

    public static Integer getClosest(GameDescription gameDescription, GameState gameState, Player player) {
        List<Planet> ownedPlanets = gameState.getPlanetStates().stream()
                .filter(planetState -> player.getUserID().equals(planetState.getOwner()))
                .filter(planetState -> planetState.getOwnershipRatio().equals(1.0))
                .map(planetState -> GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()))
                .collect(Collectors.toList());

        double mx = 0;
        double my = 0;
        for (Planet planet : ownedPlanets) {
            mx += planet.getX();
            my += planet.getY();
        }
        mx /= ownedPlanets.size();
        my /= ownedPlanets.size();

        final double MX = mx;
        final double MY = my;

        Optional<PlanetState> closestNotOur = gameState.getPlanetStates().stream()
                .filter(planetState -> !player.getUserID().equals(planetState.getOwner()))
                .min(Comparator.comparingDouble(planetState ->
                        {
                            Integer X = GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()).getX();
                            Integer Y = GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()).getY();
                            return (X - MX) * (X - MX) + (Y - MY) * (Y - MY);
                        }

                ));

        return closestNotOur.get().getPlanetID();
    }

    public static StationedArmy getStationedArmyOfPlanet(GameState gameState, Planet planet, Player player) {
        return gameState.getPlanetStates().stream()
                .filter(planetState -> planet.getPlanetID().equals(planetState.getPlanetID()))
                .map(planetState -> PlanetStateInfo.getStationedArmyOfPlayer(planetState, player))
                .findFirst()
                .get();

    }

    private static StationedArmy getStationedEnemyArmyOfPlanet(GameState gameState, Planet planet, Player player) {
        return gameState.getPlanetStates().stream()
                .filter(planetState -> planet.getPlanetID().equals(planetState.getPlanetID()))
                .map(planetState -> PlanetStateInfo.getStationedArmyOfEnemy(planetState, player))
                .findFirst()
                .get();
    }

    public static Integer getClosestWeightedWithArmies(GameDescription gameDescription, GameState gameState, Player player) {
        List<Planet> ownedPlanets = gameState.getPlanetStates().stream()
                .filter(planetState -> player.getUserID().equals(planetState.getOwner()))
                .filter(planetState -> planetState.getOwnershipRatio().equals(1.0))
                .map(planetState -> GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()))
                .collect(Collectors.toList());

        List<Planet> enemyPlanets = gameState.getPlanetStates().stream()
                .filter(planetState -> !player.getUserID().equals(planetState.getOwner()))
                .filter(planetState -> planetState.getOwnershipRatio().equals(1.0))
                .map(planetState -> GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()))
                .collect(Collectors.toList());

        double mx = 0;
        double my = 0;
        double sum = 0;
        for (Planet planet : ownedPlanets) {
            StationedArmy stationedArmy = getStationedArmyOfPlanet(gameState, planet, player);
            mx += planet.getX() * stationedArmy.getSize();
            my += planet.getY() * stationedArmy.getSize();
            sum += stationedArmy.getSize();
        }

        for (Planet planet : enemyPlanets) {
            StationedArmy stationedArmy = getStationedEnemyArmyOfPlanet(gameState, planet, player);
            mx -= planet.getX() * stationedArmy.getSize();
            my -= planet.getY() * stationedArmy.getSize();
            sum += stationedArmy.getSize();
        }

        mx /= sum;
        my /= sum;

        final double MX = mx;
        final double MY = my;

        Optional<PlanetState> closestNotOur = gameState.getPlanetStates().stream()
                .filter(planetState -> !player.getUserID().equals(planetState.getOwner()))
                .min(Comparator.comparingDouble(planetState ->
                        {
                            Integer X = GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()).getX();
                            Integer Y = GameDescriptionInfo.getPlanetWithId(gameDescription, planetState.getPlanetID()).getY();
                            return (X - MX) * (X - MX) + (Y - MY) * (Y - MY);
                        }

                ));

        return closestNotOur.get().getPlanetID();
    }


}
