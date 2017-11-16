package com.loxon.javachallenge2017.kotlinos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.loxon.javachallenge2017.pack.descriptionclasses.GameDescription
import com.loxon.javachallenge2017.pack.descriptionclasses.Player
import com.loxon.javachallenge2017.pack.stateclasses.GameState
import com.loxon.javachallenge2017.pack.stateclasses.MovingArmy
import com.loxon.javachallenge2017.pack.stateclasses.StationedArmy
import com.loxon.javachallenge2017.pack.utility.GameDescriptionInfo
import com.loxon.javachallenge2017.pack.utility.GameStateInfo

/**
 * Created by meee3 on 11/16/2017.
 */

class ImportantData(
        val gameState: GameState, val player: Player, val gameDescription: GameDescription, val discount: Double
)

class CalculateGame(
        val importantData: ImportantData,
        val planets: List<CalculatedPlanet>
) {

    companion object {
        fun create(importantData: ImportantData): CalculateGame {
            with(importantData) {
                val calculatedPlanets: List<CalculatedPlanet> = gameDescription.planets.map { planet ->
                    with(planet) {
                        CalculatedPlanet(
                                importantData = importantData,
                                id = planetID,
                                x = x,
                                y = y,
                                radius = radius,
                                neighbours = neighbours
                        ).apply {
                            val planetState = gameState.planetStates.find { it.planetID == id }!!
                            owner = planetState.owner
                            ownerShipRatio = planetState.ownershipRatio
                            stationedArmies = planetState.stationedArmies
                            movingArmies = planetState.movingArmies
                        }
                    }
                }

                return CalculateGame(importantData, calculatedPlanets).apply {
                    // Parent
                    calculatedPlanets.forEach {
                        it.parent = this
                    }
                }
            }
        }
    }

    operator fun get(planetId: Int) = planets.find { it.id == planetId }

    val playerPlanets: List<CalculatedPlanet>
        get() = planets.filter { it.owner == importantData.player.userID }

}

data class CalculatedPlanet(
        val importantData: ImportantData,
        var id: Int,
        var x: Int,
        var y: Int,
        var radius: Int,
        var neighbours: List<Int>,
        var owner: String? = null,
        var ownerShipRatio: Double = 0.0,
        var stationedArmies: List<StationedArmy>? = null,
        var movingArmies: List<MovingArmy>? = null
) {

    lateinit var parent: CalculateGame

    private fun calculate(gameState: GameState, player: Player, gameDescription: GameDescription, discount: Double) {}

    val stationedArmiesOfPlayer: List<StationedArmy>
        get() = stationedArmies!!.filter { it.owner == importantData.player.userID }

    val enemyStationedArmies: List<StationedArmy>
        get() = stationedArmies!!.filter { it.owner != importantData.player.userID }

    val incomingEnemy: List<MovingArmy>
        get() = movingArmies!!.filter { it.owner != importantData.player.userID }

    val neighbourPlanets: List<CalculatedPlanet>
        get() = neighbours.map { parent[it]!! }

    fun MovingArmy.distanceToPlanet() = GameStateInfo.distanceBetweenAB(
            x, y, this@CalculatedPlanet.x.toDouble(), this@CalculatedPlanet.y.toDouble()
    )

    val enemyStationedArmySize: Int
        get() = enemyStationedArmies.sumBy { it.size }

    val maxIncomingEnemy: Int
        get() = incomingEnemy.maxBy { it.size }!!.size

    val enemyStrength: Double
        get() {
            return enemyStationedArmies.sumBy { it.size }.toDouble() + incomingEnemy.sumByDouble {
                Math.pow(
                        importantData.discount,
                        it.distanceToPlanet().toDouble() / importantData.gameDescription.movementSpeed)
            }
        }

}
