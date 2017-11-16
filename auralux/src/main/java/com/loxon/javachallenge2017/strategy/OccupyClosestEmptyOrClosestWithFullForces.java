package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Player;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.stateclasses.PlanetState;
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;
import com.loxon.javachallenge2017.pack.utility.PlanetStateInfo;

import java.util.ArrayList;
import java.util.List;

public class OccupyClosestEmptyOrClosestWithFullForces extends Strategy {
    private Double multiplier = 0.8;

    public OccupyClosestEmptyOrClosestWithFullForces(GameDescription gameDescription) {
        super(gameDescription);
    }

    @Override
    public Response getResponse(GameState gameState) {
//        Player player = GameDescriptionInfo.getOurPlayer(gameDescription);
//        // Integer planetId = GameStateInfo.getBiggestEmptyOrWeakest(gameDescription, gameState, player);
//        // Integer planetId = GameStateInfo.getClosest(gameDescription, gameState, player);
//        Integer planetId = GameStateInfo.getClosestWeightedWithArmies(gameDescription, gameState, player);
//
//
//        List<PlanetState> stationedArmiesPlanets = GameStateInfo.getFullyOccupiedPlanetsWithStationedArmiesOfPlayer(gameState, player);
//        List<Response> responses = new ArrayList<>(stationedArmiesPlanets.size());
//        for (PlanetState planetState : stationedArmiesPlanets) {
//            StationedArmy stationedArmy = PlanetStateInfo.getStationedArmyOfPlayer(planetState, player);
//            if (stationedArmy != null) {
//                responses.add(new Response(planetState.getPlanetID(), planetId, (int) (stationedArmy.getSize()*multiplier)));
//            }
//        }
//        return responses;
        return null;
    }
}
