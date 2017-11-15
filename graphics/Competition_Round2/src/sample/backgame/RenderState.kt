package sample.backgame


interface Steppable<T> {
    val next: T
}

enum class PlanetRenderState : Steppable<PlanetRenderState> {
    MagicVisible,
    OwnerVisible,
    VisibleAll;

    override val next: PlanetRenderState
        get() = when (this) {
            MagicVisible -> VisibleAll
            OwnerVisible -> MagicVisible
            VisibleAll -> OwnerVisible
        }
}

enum class UnitRenderState : Steppable<UnitRenderState> {
    Size,
    Generic;

    override val next: UnitRenderState
        get() = when (this) {
            Size -> Generic
            Generic -> Size
        }
}

class RenderState(
        var planetRenderState: PlanetRenderState,
        var unitRenderState: UnitRenderState
) {

    fun nextPlanet() {
        planetRenderState = planetRenderState.next
    }

    fun nextUnit() {
        unitRenderState = unitRenderState.next
    }

}