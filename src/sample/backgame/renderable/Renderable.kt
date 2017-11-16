package sample.backgame.renderable

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import sample.backgame.PlanetRenderState
import sample.backgame.RenderState
import sample.backgame.UnitRenderState
import sample.game.GameStateListener
import sample.game.GameVis

interface Renderable {

    fun render(graphicsContext: GraphicsContext, renderState: RenderState)

}

val idleColor = Color.web("0xfecea8")
val magicColor = Color.web("0xff847c")
val connectionColor = Color.WHITESMOKE
val planetColors: List<Color> = listOf(Color.web("0x99b898"), Color.web("0xe84a5f"), Color.DARKVIOLET, Color.BEIGE, Color.BISQUE)
val backgroundColor = Color.web("0x2a3638")

val textColor = Color.WHITE

const val unitCircleSizeRenderMultip = 1.2

class PlanetsBackground : Renderable {

    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {
        with(graphicsContext) {
            with(graphicsContext.canvas) {
                fill = backgroundColor
                fillRect(0.0, 0.0, width, height)

                fill = textColor
                fillText(renderState.planetRenderState.toString(), 10.0, 100.0)
                fillText(renderState.unitRenderState.toString(), 10.0, 150.0)
            }

        }
    }
}

fun GraphicsContext.drawCircle(x: Double, y: Double, radius: Double) =
        fillOval(x - radius, y - radius, radius * 2, radius * 2)

fun GraphicsContext.drawSquare(x: Double, y: Double, halfSize: Double) = fillRect(x - halfSize, y - halfSize, 2 * halfSize, 2 * halfSize)


class RenderableConnection(x1: Int, y1: Int, x2: Int, y2: Int) : Renderable {

    val x1: Double
    val x2: Double
    val y1: Double
    val y2: Double

    init {
        this.x1 = x1.toDouble() * GameVis.multiplier
        this.x2 = x2.toDouble() * GameVis.multiplier
        this.y1 = y1.toDouble() * GameVis.multiplier
        this.y2 = y2.toDouble() * GameVis.multiplier
    }


    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {

        with(graphicsContext) {
            with(canvas) {
                lineWidth = GameVis.multiplier * 5.0
                stroke = connectionColor
                strokeLine(x1, y1, x2, y2)
            }
        }
    }
}

class RenderablePlanet(x: Int, y: Int, radius: Int, val id: Int) : Renderable {

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

    var magicNumber: Double = 0.0

    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {

        val o = owner
        val ownerColor = if (o == null) idleColor
        else planetColors[GameStateListener.players.indexOf(o)]

        with(graphicsContext) {
            with(canvas) {

                when (renderState.planetRenderState) {
                    PlanetRenderState.MagicVisible -> {
                        fill = magicColor
                        val magicRad = magicNumber * radius
                        drawCircle(x, y, magicRad)
                    }
                    PlanetRenderState.OwnerVisible -> {
                        fill = idleColor.interpolate(ownerColor, ownerShipRatio)
                        drawCircle(x, y, radius)

                        textAlign = TextAlignment.CENTER
                        fillText(o, x, y - 50.0 * GameVis.multiplier)
                    }
                    PlanetRenderState.VisibleAll -> {
                        // TODO
                    }
                }

                fill = textColor
                fillText(this@RenderablePlanet.id.toString(), x, y + 60.0 * GameVis.multiplier)
            }
        }
    }
}

class RenderableMovingUnits(x: Double, y: Double, val size: Int, val owner: String) : Renderable {

    val x = x * GameVis.multiplier
    val y = y * GameVis.multiplier

    val ownerColor = planetColors[GameStateListener.players.indexOf(owner)]

    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {
        with(graphicsContext) {
            with(canvas) {
                when (renderState.unitRenderState) {
                    UnitRenderState.Size -> {
                        fill = ownerColor
                        drawCircle(x, y, size * GameVis.multiplier * unitCircleSizeRenderMultip)
                    }
                    UnitRenderState.Generic -> {
                        val outerSize = 20.0 * GameVis.multiplier
                        val innerSize = 15.0 * GameVis.multiplier

                        fill = ownerColor
                        drawSquare(x, y, outerSize)

                        fill = ownerColor.brighter()
                        drawSquare(x, y, innerSize)
                    }
                }

                fillText(size.toString(), x, y - 50.0 * GameVis.multiplier)

            }
        }
    }
}

// Todo: Only this coordinate is created in screen space
class StationaryUnits(val x: Double, val y: Double, val size: Int, val owner: String) : Renderable {

    val index = GameStateListener.players.indexOf(owner)
    val ownerColor = planetColors[index]

    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {
        with(graphicsContext) {
            with(canvas) {
                fill = ownerColor

                when (renderState.unitRenderState) {
                    UnitRenderState.Size -> {
                        drawCircle(x, y, size * GameVis.multiplier * unitCircleSizeRenderMultip)
                    }
                    UnitRenderState.Generic -> {

                    }
                }

                fillText(size.toString(), x, y + 40.0 * GameVis.multiplier * (index + 2))
            }
        }
    }
}