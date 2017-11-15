package sample.backgame

import javafx.scene.canvas.GraphicsContext
import sample.backgame.renderable.PlanetsBackground
import sample.backgame.renderable.Renderable
import java.util.*
import kotlin.collections.ArrayList

object GameObjects : Renderable {

    private val backgrounds: MutableList<Renderable> = Collections.synchronizedList(ArrayList())
    val units: MutableList<Renderable> = Collections.synchronizedList(ArrayList())

    // These will not be removed
    val planets: MutableList<Renderable> = Collections.synchronizedList(ArrayList())
    val connections: MutableList<Renderable> = Collections.synchronizedList(ArrayList())

    init {
        backgrounds.add(PlanetsBackground())
    }

    override fun render(graphicsContext: GraphicsContext, renderState: RenderState) {
        backgrounds.forEach { it.render(graphicsContext, renderState) }
        connections.forEach { it.render(graphicsContext, renderState) }
        planets.forEach { it.render(graphicsContext, renderState) }
        units.forEach { it.render(graphicsContext, renderState) }
    }


}