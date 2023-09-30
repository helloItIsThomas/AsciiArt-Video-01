package classes

import GLOBAL.brightnessThreshold
import GLOBAL.cellWidth
import GLOBAL.height
import GLOBAL.sceneInterval
import GLOBAL.shapeScaler
import GLOBAL.width
import org.openrndr.color.ColorRGBa
import org.openrndr.color.mix
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.math.mix
import java.util.*

class Cell(var brightness: Double = 0.0, var color: ColorRGBa = ColorRGBa.BLUE){
    var prevBright = LinkedList<Double>()
    var prevW: Double
    var goalW: Double
    var renderW: Double
    var prevCol: ColorRGBa
    var goalCol: ColorRGBa
    var renderColor: ColorRGBa
    var position: Vector2
    var radius: Double
    lateinit var id: String
    init {
        prevW = 0.0
        goalW = 0.0
        renderW = 0.0
        prevCol = ColorRGBa.GREEN
        goalCol = ColorRGBa.GREEN
        renderColor = ColorRGBa.GREEN
        position = Vector2(width * 0.5, height * 0.5)
        radius = (renderW * shapeScaler) / 2.0
    }

    fun updateBright(_newBright: Double) {
        prevBright.addLast(_newBright)
        if(prevBright.size > 5){
            prevBright.removeFirst()
        }
    }

    fun easeOutCubic(x: Double): Double {
        val t = x - 1.0
        return t * t * t + 1.0
    }
    fun animW(_localClock: Double) {
        val normalizedClock = (_localClock) / ( sceneInterval )
        val transition = (normalizedClock) % 1
        renderW = mix(
            prevW,
            goalW,
            easeOutCubic(transition)
        )
        radius = (renderW * shapeScaler) / 2.0
        renderColor = mix(prevCol, goalCol, easeOutCubic(transition))
    }
    fun calcW() {
        prevCol = goalCol
        goalCol = color
        prevW = goalW
//                goalW = brightness.map(
//                    0.0,
//                    1.0,
//                    0.0,
//                    cellWidth.toDouble()
//                )
        if (prevBright.isNotEmpty()) {
            if(prevBright.last > brightnessThreshold){
                goalW = prevBright.last.map(
                    0.0,
                    1.0,
                    0.0,
                    cellWidth
                )
            }
        } else {
            goalW = 0.0
        }
    }
    fun checkOverlap(other: Cell) {
        var diff = position - other.position
        var distance = diff.length
        if (distance < radius + other.radius) {
            diff = diff.normalized
            diff *= (radius + other.radius - distance) / 2.0
            position += diff
            other.position -= diff
        }
    }
}