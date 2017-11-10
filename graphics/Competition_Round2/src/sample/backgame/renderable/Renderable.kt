package sample.backgame.renderable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import sample.game.GameStateListener
import sample.game.GameVis

interface Renderable {

    fun render(graphicsContext: GraphicsContext)

}

val idleColor = Color.BLUEVIOLET
val planetColors: List<Color> = listOf(Color.MOCCASIN, Color.CHARTREUSE, Color.DARKVIOLET, Color.BEIGE, Color.BISQUE)

class PlanetsBackground : Renderable {

    override fun render(graphicsContext: GraphicsContext) {
        with(graphicsContext) {
            with(graphicsContext.canvas) {
                fill = Color.DARKGRAY
                fillRect(0.0, 0.0, width, height)
            }
        }
    }
}

class RenderablePlanet(x: Int, y: Int, radius: Int) : Renderable {

    val x: Double
    val y: Double
    val radius: Double

    init {
        this.x = x.toDouble() * GameVis.multiplier
        this.y = y.toDouble() * GameVis.multiplier
        this.radius = radius.toDouble() * GameVis.multiplier
    }


    var owner: String? = null
    var ownerShipRatio: Double = 0.0

    override fun render(graphicsContext: GraphicsContext) {

        val o = owner
        val ownerColor = if (o == null) idleColor
        else planetColors[GameStateListener.players.indexOf(o)]

        with(graphicsContext) {
            with(canvas) {
                fill = idleColor.interpolate(ownerColor, ownerShipRatio)
                fillOval(x - radius,
                        y - radius,
                        radius * 2,
                        radius * 2
                )

                textAlign = TextAlignment.CENTER
                fillText(o, x, y - 50.0 * GameVis.multiplier)
            }
        }
    }
}

class RenderableMovingUnits(x: Double, y: Double, val size: Int, val owner: String) : Renderable {

    val x = x * GameVis.multiplier
    val y = y * GameVis.multiplier

    val ownerColor = planetColors[GameStateListener.players.indexOf(owner)]

    override fun render(graphicsContext: GraphicsContext) {
        with(graphicsContext) {
            with(canvas) {

                val outerSize = 20.0 * GameVis.multiplier
                val innerSize = 15.0 * GameVis.multiplier

                fill = Color.BLACK
                fillRect(x - outerSize, y - outerSize, 2 * outerSize, 2 * outerSize)

                fill = ownerColor
                fillRect(x - innerSize, y - innerSize, 2 * innerSize, 2 * innerSize)

                fillText(size.toString(), x, y - 50.0 * GameVis.multiplier)
            }
        }
    }
}

// Todo: Only this coordinate is created in screen space
class StationaryUnits(val x: Double, val y: Double, val size: Int, val owner: String) : Renderable {

    val index = GameStateListener.players.indexOf(owner)
    val ownerColor = planetColors[index]

    override fun render(graphicsContext: GraphicsContext) {
        with(graphicsContext) {
            with(canvas) {
                fill = ownerColor
                fillText(size.toString(), x, y + 40.0 * GameVis.multiplier * (index + 2))
            }
        }
    }
}