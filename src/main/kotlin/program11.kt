//
//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.color.mix
//import org.openrndr.draw.*
//import org.openrndr.drawComposition
//import org.openrndr.extra.olive.oliveProgram
//import org.openrndr.ffmpeg.ScreenRecorder
//import org.openrndr.math.IntVector2
//import org.openrndr.math.Vector2
//import org.openrndr.math.map
//import org.openrndr.math.mix
//import org.openrndr.shape.Circle
//import org.openrndr.shape.CompositionDrawer
//import java.util.*
//import kotlin.system.measureTimeMillis
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
//    oliveProgram {
//        val imgCount = 53
//        val brightnessThreshold = 0.0
//        val shapeScaler = 1.0
//        var thisClock: Double
//        // I think adjusting clockDiv adjusts the framerate
//        val clockDiv = 0.5  // clockDiv of 0.05 means 20 frames between scenes I think
//        val framesBtwnScenes = ((1.0 / clockDiv)) // this should mean how many frames between new scene
//        // I think adjusting sceneInterval adjusts the sample rate
//        val sceneInterval = 1  // this should mean how many intervals should pass between drawing a scene
//        val framesBetweenSceneIntervals = framesBtwnScenes * sceneInterval
//
//        // new scene = thisClock.toInt() != the last thisClock.toInt(),
//        // "fire scene interval" when newSceneCounter >= sceneInterval
//        val ratio = width.toDouble() / height.toDouble()
//        val imgs = mutableListOf<ColorBuffer>()
//        for(i in 1 until imgCount) {
//            imgs.add(loadImage("data/images/standFrames0/frame-$i.png"))
//        }
//        val gridWidth = 100.0      // Number of grid units in width
//        val gridHeight = (gridWidth / ratio)      // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//        // Ok now I want to store the previous brightness level as well as the current brightness level.
//        // actually, I want to store the previous n brightness levels as well as the current.
//        // this number should be static for simplicity for now
//        // maybe i can have a 5 unit brightness list, and use the push data struct.
//        // so the one on the top is always the most recent one.
//        class Cell(var brightness: Double = 0.0, var color: ColorRGBa = ColorRGBa.BLUE){
//            var prevBright = LinkedList<Double>()
//            var prevW: Double
//            var goalW: Double
//            var renderW: Double
//            var prevCol: ColorRGBa
//            var goalCol: ColorRGBa
//            var renderColor: ColorRGBa
//            var position: Vector2
//            var radius: Double
//            lateinit var id: String
//            init {
//                prevW = 0.0
//                goalW = 0.0
//                renderW = 0.0
//                prevCol = ColorRGBa.GREEN
//                goalCol = ColorRGBa.GREEN
//                renderColor = ColorRGBa.GREEN
//                position = Vector2(width * 0.5, height * 0.5)
//                radius = (renderW * shapeScaler) / 2.0
//            }
//
//            fun updateBright(_newBright: Double) {
//                prevBright.addLast(_newBright)
//                if(prevBright.size > 5){
//                    prevBright.removeFirst()
//                }
//            }
//
//            fun easeOutCubic(x: Double): Double {
//                val t = x - 1.0
//                return t * t * t + 1.0
//            }
//            fun animW(_localClock: Double) {
//                val normalizedClock = (_localClock) / ( sceneInterval )
//                val transition = (normalizedClock) % 1
//                renderW = mix(
//                    prevW,
//                    goalW,
//                    easeOutCubic(transition)
//                )
//                radius = (renderW * shapeScaler) / 2.0
//                renderColor = mix(prevCol, goalCol, easeOutCubic(transition))
//            }
//            fun calcW() {
//                prevCol = goalCol
//                goalCol = color
//                prevW = goalW
////                goalW = brightness.map(
////                    0.0,
////                    1.0,
////                    0.0,
////                    cellWidth.toDouble()
////                )
//                if (prevBright.isNotEmpty()) {
//                    if(prevBright.last > brightnessThreshold){
//                        goalW = prevBright.last.map(
//                            0.0,
//                            1.0,
//                            0.0,
//                            cellWidth.toDouble()
//                        )
//                    }
//                } else {
//                    goalW = 0.0
//                }
//            }
//            fun checkOverlap(other: Cell) {
//                var diff = position - other.position
//                var distance = diff.length
//                if (distance < radius + other.radius) {
//                    diff = diff.normalized
//                    diff *= (radius + other.radius - distance) / 2.0
//                    position += diff
//                    other.position -= diff
//                }
//            }
//
//        }
//        val brightnessValues = Array(gridWidth.toInt()) { Array(gridHeight.toInt()) { Cell() } }
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
//                                value.updateBright(value.brightness)
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
////                        value.prevBright.forEach { n ->
////                            println(n)
////                        }
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
////        extend(ScreenRecorder()) {
////            contentScale = 4.0
////            frameRate = 60
////            maximumDuration =  90.0
////        }
//
//        val indices = (0 until gridWidth.toInt()).flatMap { i ->
//            (0 until gridHeight.toInt()).map { j -> Pair(i, j) }
//        }
//
//        extend {
//
//            thisClock = frameCount * clockDiv
//
//            drawer.clear(ColorRGBa.BLACK)
//
//            currentImg = imgs[(thisClock).toInt() % imgs.size]
////            drawer.image(currentImg, 0.0, 0.0)
//            val timeMillis = measureTimeMillis {
//                var idIncrementor = 0
//                indices.forEach { (i, j) ->
//                    currentColor = currentImg.shadow.read(
//                        i * cellWidth.toInt(), j * cellHeight.toInt()
//                    )
//                    val brightness =
//                        0.2126 * currentColor.r
//                    +0.7152 * currentColor.g
//                    +0.0722 * currentColor.b
//                    brightnessValues[i][j].id = idIncrementor++.toString() //   i can move this to the main block
//                    brightnessValues[i][j].brightness = brightness
//                    brightnessValues[i][j].color = currentColor.toSRGB()
//                }
//
//                var circleList: MutableList<Circle> = mutableListOf()
//                testFlag.check(thisClock, sceneInterval)
//                indices.forEach { (i, j) ->
//                    brightnessValues[i][j].position = Vector2((i * cellWidth.toDouble()), (j * cellHeight.toDouble()))
//                    drawer.fill = brightnessValues[i][j].renderColor
//                    circleList.add(
//                        Circle(
//                            brightnessValues[i][j].position.x,
//                            brightnessValues[i][j].position.y,
//                            brightnessValues[i][j].renderW * shapeScaler
//                        )
//                    )
//                }
//                drawer.stroke = null
//                drawer.pushTransforms()
//                drawer.translate(circleList[0].radius * 2, circleList[0].radius * 2)
//                drawer.circles(circleList)
//                drawer.popTransforms()
//            }
//
//            println(timeMillis)
//
//            drawer.fill = null
//            drawer.stroke = ColorRGBa.WHITE
//            drawer.strokeWeight = 0.25
//            drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
//            drawer.stroke = null
//
//        }
//    }
//}
//
