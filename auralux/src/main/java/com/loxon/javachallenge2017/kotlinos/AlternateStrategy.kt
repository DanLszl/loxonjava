package com.loxon.javachallenge2017.kotlinos

import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription
import com.loxon.javachallenge2017.pack.responses.Response
import com.loxon.javachallenge2017.pack.stateclasses.GameState
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo
import com.loxon.javachallenge2017.strategy.Strategy
import com.loxon.javachallenge2017.strategy.UltimateStrategy
import java.util.*

class AlternateStrategy(gameDescription: GameDescription) : Strategy(gameDescription) {

    val ultimateStrategy: UltimateStrategy = UltimateStrategy(gameDescription)

    override fun getResponse(gameState: GameState): Response {
        val magic: Map<Int, Double> = ultimateStrategy.calculateMagicNumbers(gameState)

        val game = CalculateGame.create(ImportantData(
                gameState,
                GameDescriptionInfo.getOurPlayer(gameDescription),
                gameDescription,
                1.1)
        )

        // Tamadjak a bolygonkat - tulerovel
        // magic alapjan barat / kamikaze

        for (planet in game.playerPlanets) {
            with(planet) {
                val armySize = stationedArmiesOfPlayer.sumBy { it.size }
                if (maxIncomingEnemy > armySize || enemyStationedArmySize > armySize) {
                    val chosenNeighbour = neighbours.maxBy { magic[it]!! }
                    return Response(id, chosenNeighbour, armySize)
                }

                val smallestEnemyPlanet = neighbourPlanets.minBy { it.enemyStationedArmySize }!!
                val smallestEnemyArmy = smallestEnemyPlanet.enemyStationedArmySize
                if (smallestEnemyArmy < armySize * 0.75) {

                    val enemyIncomingSize = smallestEnemyPlanet.incomingEnemy.sumBy { it.size }

                    if (enemyIncomingSize + smallestEnemyArmy < armySize)
                        return Response(id, smallestEnemyPlanet.id, armySize)
                }
            }
        }

        ultimateStrategy.magicNumbers = magic

        return ultimateStrategy.getResponse(gameState)
    }
}