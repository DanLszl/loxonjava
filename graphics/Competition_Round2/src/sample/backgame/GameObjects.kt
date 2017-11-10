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

    init {
        backgrounds.add(PlanetsBackground())
    }

    override fun render(graphicsContext: GraphicsContext) {
//        println("GameObject rendering: ")
//        println(planets)

        backgrounds.forEach { it.render(graphicsContext) }
        planets.forEach { it.render(graphicsContext) }
        units.forEach { it.render(graphicsContext) }
    }


}