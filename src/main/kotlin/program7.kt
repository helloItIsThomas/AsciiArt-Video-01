//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.draw.ColorBuffer
//import org.openrndr.draw.loadImage
//import org.openrndr.extra.olive.oliveProgram
//import org.openrndr.math.IntVector2
//import org.openrndr.math.map
//import org.openrndr.math.mix
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
////        val clockDiv = 0.25
//
//        // clockDiv of 0.05 means 20 frames between scenes I think
//        val clockDiv = 0.5
//        // this should mean how many images / scenes display in between intervals
//        val sceneInterval = 20
//
//        // when thisClock.toInt() != the last thisClock.toInt(),
//        // there is a new scene.
//        // when the number of new scenes hits sceneInterval,
//        // draw the current scene.
//
//        // (frameCount * clockDiv)
//
//        // framesBtwnScenes seems correct
//        var framesBtwnScenes = ((1.0 / clockDiv) * sceneInterval )
//
//        val ratio = width / height
//        val imgs = mutableListOf<ColorBuffer>()
//        for(i in 1 until 550) {
//            imgs.add(loadImage("data/images/frames2/img$i.png"))
//        }
//        val img = loadImage("data/images/grid.png")
//        val gridWidth = 20 // Number of grid units in width
//        val gridHeight = gridWidth * ratio // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//        class Cell(var brightness: Double = 0.0, var color: ColorRGBa = ColorRGBa.BLACK){
//            var prevW: Double
//            var goalW: Double
//            var renderW: Double
//            var renderColor: ColorRGBa
//            lateinit var id: String
//            init {
//                prevW = 0.0
//                goalW = 0.0
//                renderW = 0.0
//                renderColor = ColorRGBa.GREEN
//            }
//
//            fun easeOutCubic(x: Double): Double {
//                val t = x - 1.0
//                return t * t * t + 1.0
//            }
//            fun animW(_localClock: Double) {
//                val normalizedClock = _localClock / ( sceneInterval )
//                val transition = normalizedClock % 1
//                renderW = mix(
//                    prevW,
//                    goalW,
//                    easeOutCubic(transition)
//                )
//            }
//            fun calcW() {
//                prevW = goalW
//                // prevW = goalW  •   here seems to be correct,
//                // storing the previous goalWidth as prevW.
//                // moving  • // prevW = renderW    somewhere else because
//                // here it does not store the goalW of the last drawn scene as prevW,
//                // it stores the drawn scene - 1.
//
////                renderColor = color
//                renderColor = ColorRGBa.YELLOW
//                goalW = brightness.map(
//                    0.0,
//                    1.0,
//                    0.0,
//                    cellWidth.toDouble()
//                )
//            }
//        }
//        val brightnessValues = Array(gridWidth) { Array<Cell>(gridHeight) { Cell() } }
//        class Flag {
//            var storedTime: Int
//            var counter: Int
//            var sceneCounter: Int
//            var maxVal = 0.0
//            init {
//                storedTime = 0
//                counter = 0
//                sceneCounter = 0
//            }
//            fun check(_updateClockRef: Double, _sceneInterval: Int){
//
//                if(storedTime != _updateClockRef.toInt()){
//                    // FIRE
//                    println("                 fire: $frameCount")
//                    storedTime = _updateClockRef.toInt()
//                    counter = 0
//                    sceneCounter++
//                    if(sceneCounter >= _sceneInterval){
//                        println("Fire Scene Interval")
//                        brightnessValues.forEachIndexed { i, row ->
//                            row.forEachIndexed { j, value ->
//                                value.calcW()
//                            }
//                        }
//                        sceneCounter = 0
//                    }
//                } else {
//                    counter++
//                }
//
//                // TRYING IT HERE
//                brightnessValues.forEachIndexed { i, row ->
//                    row.forEachIndexed { j, value ->
//                        value.animW(_updateClockRef)
//                    }
//                }
//                //
//            }
//        }
//
//        var testFlag = Flag()
//        var currentImg: ColorBuffer
//        var currentColor: ColorRGBa
//        imgs.forEach{it.shadow.download()}
//
//        extend {
//            drawer.clear(ColorRGBa.BLACK)
//
//            thisClock = frameCount * clockDiv
//            currentImg = imgs[(thisClock).toInt() % imgs.size]
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
//                    // right now I am creating a new Cell in every draw loop.
//                    // I want to create a nested array of Cells once, then update them.
//                    // this is old  •  brightnessValues[i][j] = Cell(brightness, currentColor)
//                    brightnessValues[i][j].id = (i+j).toString()
//                    brightnessValues[i][j].brightness = brightness
//                    brightnessValues[i][j].color = currentColor
//                }
//            }
//
//            testFlag.check(thisClock, sceneInterval)
//
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    drawer.pushTransforms()
//                    drawer.fill = brightnessValues[i][j].renderColor
//                    drawer.circle(
//                        (i * cellWidth.toDouble()) + cellWidth.toDouble() * 0.5,
//                        (j * cellHeight.toDouble()),
//                        brightnessValues[i][j].renderW
//                    )
//                    drawer.popTransforms()
//                }
//            }
//        }
//    }
//}
