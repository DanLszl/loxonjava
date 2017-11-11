package sample.game

import com.google.gson.Gson
import javafx.application.Platform
import sample.backgame.GameObjects
import sample.backgame.renderable.RenderableMovingUnits
import sample.backgame.renderable.RenderablePlanet
import sample.backgame.renderable.StationaryUnits
import sample.pack.descriptionclasses.GameDescription
import sample.pack.stateclasses.GameState
import java.util.concurrent.atomic.AtomicBoolean

object GameStateListener : GameChangeListener {

    private var first = true

    val gson = Gson()

    private val planets: MutableMap<Int, RenderablePlanet> = HashMap()

    // Adding is only ok from here
    val players: MutableList<String> = ArrayList()

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
                    planets[planetState.planetID].apply {
                        this as RenderablePlanet
                        owner = planetState.owner
                        ownerShipRatio = planetState.ownershipRatio
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

    override fun onMessage(message: String) {
//        println("onMessage !!!")

        var proceeded = false

        try {
            if (handlingMessage.getAndSet(true))
                return

            proceeded = true

            updateGame(message)

//            println("Thread id request: " + Thread.currentThread().id)

            Platform.runLater {
                val context = GameVis.graphicsContext
                if (context != null) {
//                    println("Thread id rendering: " + Thread.currentThread().id)
                    GameObjects.render(context)
                }
            }

        } finally {

            if (proceeded)
                handlingMessage.set(false)
        }
    }

}