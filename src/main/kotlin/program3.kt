//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.draw.*
//import org.openrndr.extra.olive.oliveProgram
//import org.openrndr.math.*
//import java.lang.Math.sin
//import kotlin.properties.Delegates
//
//fun main() = application {
//    configure {
//        width = 426
//        height = 240
//        hideWindowDecorations = true
//        windowAlwaysOnTop = true
//        windowTransparent = true
//        position = IntVector2(1400,100)
//    }
//
//    oliveProgram {
//        var thisClock: Double
//        val clockDiv = 0.25
//        val ratio = width / height
//        val imgs = mutableListOf<ColorBuffer>()
//        for(i in 1 until 550) {
//            imgs.add(loadImage("data/images/frames2/img$i.png"))
//        }
//        val img = loadImage("data/images/grid.png")
//        val gridWidth = 10 // Number of grid units in width
//        val gridHeight = gridWidth * ratio // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//
//        class Flag(val _startClockRef: Double) {
//            var storedTime: Int
//            var counter: Int
//            var maxVal = 0.0
//            init {
//                storedTime = _startClockRef.toInt()
//                counter = 0
//            }
//            fun check(_updateClockRef: Double){
//                if(storedTime != _updateClockRef.toInt()){
//                    // FIRE
//                    storedTime = _updateClockRef.toInt()
//                    counter = 0
//                } else counter++
//            }
//            fun update(): Double {
//                // code for determining the max val here
//                if(counter > maxVal){
//                    maxVal = counter.toDouble()
//                }
//                return counter.toDouble().map(
//                    0.0,
//                    maxVal,
//                    0.0,
//                    1.0
//                )
//            }
//        }
//
//        var testFlag = Flag(0.0)
//
//        class Cell(var brightness: Double = 0.0, var color: ColorRGBa = ColorRGBa.BLACK){
//            var prevW: Double
//            var goalW: Double
//            var w: Double
//            init {
//                prevW = 0.0
//                goalW = 0.0
//                w = 0.0
//            }
//            fun calcW(){
//                goalW = brightness.map(
//                    0.0,
//                    1.0,
//                    0.0,
//                    cellWidth.toDouble()
//                )
//                w = goalW
//            }
//        }
//        val brightnessValues = Array(gridWidth) { Array<Cell>(gridHeight) { Cell() } }
//        var currentImg: ColorBuffer
//        var currentColor: ColorRGBa
//        imgs.forEach{it.shadow.download()}
//
//        extend {
//            drawer.clear(ColorRGBa.TRANSPARENT)
//            thisClock = frameCount * clockDiv
//            currentImg = imgs[(thisClock).toInt() % imgs.size]
//            testFlag.check(thisClock)
//
//            // get the current color for each
//            // get the current brightness for each
//            // store color and brightness into Cell struct
//            // calc the width for the cell based on the brightness
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    currentColor = currentImg.shadow.read(
//                        i * cellWidth, j * cellHeight
//                    )
//                    val brightness =
//                        0.2126 * currentColor.r
//                    + 0.7152 * currentColor.g
//                    + 0.0722 * currentColor.b
//                    brightnessValues[i][j] = Cell(brightness, currentColor)
//                    brightnessValues[i][j].calcW()
//                }
//            }
//            var norm = testFlag.update()
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    drawer.pushTransforms()
//                    drawer.fill = brightnessValues[i][j].color
//                    drawer.circle(
//                        (i * cellWidth.toDouble()) + cellWidth.toDouble() * 0.5,
//                        (j * cellHeight.toDouble()),
//                        brightnessValues[i][j].w
//                    )
//                    drawer.popTransforms()
//                }
//            }
//            drawer.fill = null
//            drawer.strokeWeight = 0.75
//            drawer.stroke = ColorRGBa.WHITE
//            drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
//            drawer.stroke = null
//        }
//    }
//}
