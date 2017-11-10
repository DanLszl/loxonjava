package sample.game

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.stage.Stage
import sample.ClientEndpoint
import sample.backgame.GameObjects

class GameVis : Application() {

    fun addObjects() {
//        GameObjects.units.add(RenderablePlanet(240, 200, 45, Color.RED))
//        GameObjects.units.add(RenderablePlanet(960, 700, 45, Color.GREEN))
//        GameObjects.units.add(RenderablePlanet(120, 460, 40))
//        GameObjects.units.add(RenderablePlanet(1080, 440, 40))
//        GameObjects.units.add(RenderablePlanet(360, 420, 40))
//        GameObjects.units.add(RenderablePlanet(840, 480, 40))
//        GameObjects.units.add(RenderablePlanet(600, 260, 50))
//        GameObjects.units.add(RenderablePlanet(600, 640, 50))
//        GameObjects.units.add(RenderablePlanet(940, 120, 45))
//        GameObjects.units.add(RenderablePlanet(260, 780, 45))
    }



    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val canvas = Canvas(width, height)
        graphicsContext = canvas.graphicsContext2D


        with(primaryStage) {
            title = "Starships"
            scene = Scene(Group(canvas))
            show()
        }

//        GameObjects.render(graphicsContext)
    }

    companion object {

        val multiplier = 0.8

        val width = 1200.0 * multiplier
        val height = 900.0 * multiplier

        @JvmStatic
        fun startVisualization() {
            launch(GameVis::class.java)
        }

        var graphicsContext: GraphicsContext? = null

    }
}
