package com.loxon.javachallenge2017.strategy;

import com.loxon.javachallenge2017.features.EnemyAttackConcentration;
import com.loxon.javachallenge2017.features.PlanetArmiesStrength;
import com.loxon.javachallenge2017.features.PlanetRadii;
import com.loxon.javachallenge2017.features.Possession;
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription;
import com.loxon.javachallenge2017.pack.descriptionclasses.Planet;
import com.loxon.javachallenge2017.pack.responses.Response;
import com.loxon.javachallenge2017.pack.stateclasses.GameState;
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo;
import com.loxon.javachallenge2017.pack.utility.GameStateInfo;

import java.util.List;
import java.util.Map;
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

        enemyAttackValues.entrySet().stream()
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

        return null;
    }
}
