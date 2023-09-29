//
//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.color.mix
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
//        position = IntVector2(100,100)
//    }
//
//    program {
//        var thisClock: Double
//        // I think adjusting clockDiv adjusts the framerate
//        val clockDiv = 0.1  // clockDiv of 0.05 means 20 frames between scenes I think
//        val framesBtwnScenes = ((1.0 / clockDiv)) // this should mean how many frames between new scene
//        // I think adjusting sceneInterval adjusts the sample rate
//        val sceneInterval = 2  // this should mean how many intervals should pass between drawing a scene
//        val framesBetweenSceneIntervals = framesBtwnScenes * sceneInterval
////        println("framesBtwnScenes: " + framesBtwnScenes)
////        println("framesBetweenSceneIntervals: " + framesBetweenSceneIntervals)
//
//        // new scene = thisClock.toInt() != the last thisClock.toInt(),
//        // "fire scene interval" when newSceneCounter >= sceneInterval
//        val ratio = width / height
//        val imgs = mutableListOf<ColorBuffer>()
//        for(i in 94 until 550) {
//            imgs.add(loadImage("data/images/frames2/img$i.png"))
//        }
//        val gridWidth = 25      // Number of grid units in width
//        val gridHeight = gridWidth * ratio      // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//        class Cell(var brightness: Double = 0.0, var color: ColorRGBa = ColorRGBa.BLUE){
//            var prevW: Double
//            var goalW: Double
//            var renderW: Double
//            var prevCol: ColorRGBa
//            var goalCol: ColorRGBa
//            var renderColor: ColorRGBa
//            lateinit var id: String
//            init {
//                prevW = 0.0
//                goalW = 0.0
//                renderW = 0.0
//                prevCol = ColorRGBa.GREEN
//                goalCol = ColorRGBa.GREEN
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
//                renderColor = mix(prevCol, goalCol, easeOutCubic(transition))
//            }
//            fun calcW() {
//                prevCol = goalCol
//                goalCol = color
//                prevW = goalW
//                goalW = brightness.map(
//                    0.0,
//                    1.0,
//                    0.0,
//                    cellWidth.toDouble()
//                )
//            }
//        }
//        val brightnessValues = Array(gridWidth) { Array(gridHeight) { Cell() } }
//        class Flag {
//            var storedTime: Int
//            var counter: Int
//            var sceneCounter: Int
//
//            init {
//                storedTime = 0
//                counter = 0
//                sceneCounter = 0
//            }
//            fun check(_updateClockRef: Double, _sceneInterval: Int){
//                if(storedTime != _updateClockRef.toInt()){
//                    // FIRE
////                    println("                 fire: $frameCount")
//                    storedTime = _updateClockRef.toInt()
//                    counter = 0
//                    sceneCounter++
//                    if(sceneCounter >= _sceneInterval){
////                        println("           Fire Scene Interval")
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
//                brightnessValues.forEachIndexed { i, row ->
//                    row.forEachIndexed { j, value ->
//                        value.animW(_updateClockRef)
//                    }
//                }
//            }
//        }
//
//        var testFlag = Flag()
//        var currentImg: ColorBuffer
//        var currentColor: ColorRGBa
//        imgs.forEach{it.shadow.download()}
//
//        extend {
//            thisClock = frameCount * clockDiv
//
//            drawer.clear(ColorRGBa.BLACK)
//            currentImg = imgs[(thisClock).toInt() % imgs.size]
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    currentColor = currentImg.shadow.read(
//                        i * cellWidth, j * cellHeight
//                    )
//                    val brightness =
//                        0.2126 * currentColor.r
//                    + 0.7152 * currentColor.g
//                    + 0.0722 * currentColor.b
//                    brightnessValues[i][j].id = (i+j).toString()
//                    brightnessValues[i][j].brightness = brightness
//                    brightnessValues[i][j].color = currentColor.toSRGB()
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
//            drawer.fill = null
//            drawer.stroke = ColorRGBa.WHITE
//            drawer.strokeWeight = 0.25
//            drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
//            drawer.stroke = null
//        }
//    }
//}
//
