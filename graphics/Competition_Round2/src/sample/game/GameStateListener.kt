package sample.game

import com.google.gson.Gson
import javafx.application.Platform
import sample.backgame.GameObjects
import sample.backgame.renderable.RenderableConnection
import sample.backgame.renderable.RenderableMovingUnits
import sample.backgame.renderable.RenderablePlanet
import sample.backgame.renderable.StationaryUnits
import sample.pack.descriptionclasses.GameDescription

import sample.pack.magicnumber.MagicData
import sample.pack.stateclasses.GameState
import java.util.concurrent.atomic.AtomicBoolean

object GameStateListener : GameChangeListener {

    private var first = true

    val gson = Gson()

    private val planets: MutableMap<Int, RenderablePlanet> = HashMap()

    // Adding is only ok from here
    val players: MutableList<String> = ArrayList()

    var magic: MagicData? = null

    private val handlingMessage = AtomicBoolean(false)

    private fun updateGame(message: String) {

        try {
            if (first) {
                first = false

//                println("First")
                val gameDescription = gson.fromJson(message, GameDescription::class.java)

//                println("Game description is \n${gson.toJson(gameDescription)}")

//                println("Planets count: ${gameDescription.planets.size}")

                for (planet in gameDescription.planets) {
                    val renderPlanet = RenderablePlanet(
                            planet.x, planet.y, planet.radius, planet.planetID
                    )

//                    println("Adding planet: ${gson.toJson(this)}")

                    planets.put(planet.planetID, renderPlanet)
                    GameObjects.planets.add(renderPlanet)
                }

                // After putting the connections inside
                for (planet in gameDescription.planets) {
                    for (neighbour in planet.neighbours) {
                        val other: RenderablePlanet = planets[neighbour]!!

                        GameObjects.connections.add(
                                RenderableConnection(
                                        planet.x, planet.y,
                                        (other.x / GameVis.multiplier).toInt(),
                                        (other.y / GameVis.multiplier).toInt()
                                )
                        )
                    }
                }

                for (player in gameDescription.players) {
                    players.add(player.userID)
//                    println("Adding player: ${gson.toJson(player)}")
                }

            } else {
//                println("Update")

                val gameState = gson.fromJson(message, GameState::class.java)

//                println("Game state is \n${gson.toJson(gameState)}")

                // Clears the units from the previous round
                GameObjects.units.clear()

                // Set the owner
                for (planetState in gameState.planetStates) {
                    planets[planetState.planetID]!!.apply {
                        owner = planetState.owner
                        ownerShipRatio = planetState.ownershipRatio

                        // magicNumber = planetState.magicNumber
                        val m = magic
                        if (m != null) {
                            val list = m.magicValues

                            val mag = list.find { it.planetIndex == id }

                            if(mag == null) {
                                throw RuntimeException("Error with id: ${list.joinToString()}} and $id")
                            }

                            magicNumber = mag.magicness
                        }
                    }

                    for (movingArmy in planetState.movingArmies) {
                        with(movingArmy) {
                            GameObjects.units.add(
                                    RenderableMovingUnits(
                                            x.toDouble(),
                                            y.toDouble(),
                                            size,
                                            owner)
                            )
                        }
                    }

                    val planet = planets[planetState.planetID]!!

                    for (stationaryArmy in planetState.stationedArmies) {
                        with(stationaryArmy) {
                            GameObjects.units.add(
                                    StationaryUnits(
                                            planet.x,
                                            planet.y,
                                            stationaryArmy.size,
                                            stationaryArmy.owner
                                    )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun trySetMagic(message: String): Boolean {
        try {
            val magicData: MagicData? = gson.fromJson(message, MagicData::class.java)

            // Parse failed technically
            if (magicData == null || magicData.magicValues.size == 0)
                return false

            magic = magicData
            return true
        } catch (e: Exception) {
            // That's gonna happen a lot
        }

        return false
    }

    override fun onMessage(message: String) {
//        println("onMessage !!!")

        var proceeded = false

        try {
            if (handlingMessage.getAndSet(true))
                return

            proceeded = true

            if (!trySetMagic(message))
                updateGame(message)

//            println("Thread id request: " + Thread.currentThread().id)

            Platform.runLater {
                val context = GameVis.graphicsContext
                val renderState = GameVis.renderState
                if (context != null) {
//                    println("Thread id rendering: " + Thread.currentThread().id)
                    GameObjects.render(context, renderState)
                }
            }

        } finally {

            if (proceeded)
                handlingMessage.set(false)
        }
    }

}