import GLOBAL.cellHeight
import GLOBAL.cellWidth
import GLOBAL.sceneInterval
import GLOBAL.shapeScaler
import classes.Cell
import classes.Flag
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.draw.colorBuffer
import org.openrndr.extra.color.presets.LIGHT_GREEN
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.math.mix
import org.openrndr.shape.Circle
import java.io.File

object GLOBAL {
    lateinit var cellArray: Array<Array<Cell>>
    var width: Int = 0
    var height: Int = 0
    var shapeScaler: Double = 0.0
    var sceneInterval: Int = 0
    var brightnessThreshold: Double = 0.0
    var cellWidth: Double = 0.0
    var cellHeight: Double = 0.0
}

fun main() = application {
    configure {
        width = 540 // 1080
        height = 960 // 1920
        hideWindowDecorations = true
        windowAlwaysOnTop = true
        windowTransparent = true
        position = IntVector2(100,100)
    }

    oliveProgram {
        GLOBAL.width = width
        GLOBAL.height = height
        val imgCount = 259
        GLOBAL.brightnessThreshold = 0.0
        GLOBAL.shapeScaler = 0.1

        var thisClock: Double
        // I think adjusting clockDiv adjusts the framerate
//        val clockDiv = 0.25  // clockDiv of 0.05 means 20 frames between scenes I think
        val clockDiv = 0.3888  // clockDiv of 0.05 means 20 frames between scenes I think
        val framesBtwnScenes = ((1.0 / clockDiv)) // this should mean how many frames between new scene
        // I think adjusting sceneInterval adjusts the sample rate
        GLOBAL.sceneInterval = 50  // this should mean how many intervals should pass between drawing a scene
        val framesBetweenSceneIntervals = framesBtwnScenes * sceneInterval

        // "fire scene interval" when newSceneCounter >= sceneInterval
        val ratio = width.toDouble() / height.toDouble()
//        for(i in 1 until imgCount) {
//            imgs.add(loadImage("data/images/sFrames/$i.png"))
//        }
        val imageFiles0 = File("data/images/sFrames").listFiles { _, name -> name.endsWith(".png") }?.sorted()
        val imgs: List<ColorBuffer> = imageFiles0!!.map { loadImage(it) }

//        val imageFiles = File("data/images/lidFrames").listFiles { _, name -> name.endsWith(".png") }?.sorted()
        val imageFiles = File("data/images/frames7").listFiles { _, name -> name.endsWith(".png") }?.sorted()
        val lidImgs: List<ColorBuffer> = imageFiles!!.map { loadImage(it) }

        val gridWidth = 15.0      // Number of grid units in width
        val gridHeight = (gridWidth / ratio)      // Number of grid units in height

        GLOBAL.cellWidth = width / gridWidth
        GLOBAL.cellHeight = height / gridHeight

        GLOBAL.cellArray = Array(gridWidth.toInt()) { Array(gridHeight.toInt()) { Cell() } }

        var testFlag = Flag()
        var currentImg: ColorBuffer
        var currentColor: ColorRGBa
        imgs.forEach{it.shadow.download()}

        val indices = (0 until gridWidth.toInt()).flatMap { i ->
            (0 until gridHeight.toInt()).map { j -> Pair(i, j) }
        }

        currentImg = imgs[0]

        val rotatedImg = renderTarget(width, height){
            colorBuffer()
        }

        fun tweenWithOffset(t: Double, offset: Double): Double {
            val smoothT = t * t * (3 - 2 * t)
            val result = smoothT + offset * (1 - smoothT)
            return result.coerceIn(0.0, 1.0)
        }


        // Now you can use rotatedImg.shadow.read(...) instead of currentImg.shadow.read(...) in your loop

        extend {
            thisClock = frameCount * clockDiv
            drawer.isolatedWithTarget(rotatedImg) {
//                clear(ColorRGBa.BLACK)
//                rotate(90.0)
//                image(currentImg, 0.0, -height.toDouble(), width = height.toDouble(), height = width.toDouble())
                currentImg = imgs[(thisClock).toInt() % imgs.size]
                image(
                    currentImg,
                    0.0,
                    0.0,
//                    -height.toDouble(),
                    width = width.toDouble(),
                    height = height.toDouble()
                )
            }
            rotatedImg.colorBuffer(0).shadow.download()

//            currentImg = imgs[(thisClock).toInt() % imgs.size]
            currentImg = rotatedImg.colorBuffer(0)
            drawer.clear(ColorRGBa.BLACK)
//            drawer.image(currentImg, 0.0, 0.0)

            var idIncrementor = 0
            indices.forEach { (i, j) ->
                val cell = GLOBAL.cellArray[i][j]
                var prevBrightness = cell.brightness // Store previous brightness

                currentColor = rotatedImg.colorBuffer(0).shadow.read(
                    (i * cellWidth.toInt())%currentImg.width, (j * cellHeight.toInt())%currentImg.height
                )
                val brightness =
                    0.2126 * currentColor.r
                + 0.7152 * currentColor.g
                + 0.0722 * currentColor.b

//                var tValue = mix(prevBrightness, brightness, (frameCount*0.05) % 1.0)

                cell.id = idIncrementor++.toString()
                // cell.brightness = // brightness
                prevBrightness = prevBrightness // Set previous brightness
                cell.brightness = brightness
                cell.color = currentColor.toSRGB()
                cell.position = Vector2((i * cellWidth), (j * cellHeight))
                // +++


                // TRY COMMENTING ME BACK
                cell.localPathslider = tweenWithOffset(
//                    t = (i * j).toDouble().map(
//                        0.0,
//                        indices.size.toDouble(),
//                        0.0,
//                        5.0
//                    ),
                    (( thisClock) % 1.0),
                    offset = (( (i * j)+thisClock) % 1.0)
                )
                cell.brightness = mix(prevBrightness, brightness, (cell.localPathslider))
            }
            testFlag.check(thisClock, sceneInterval)

//            var tValue = mix(prevBrightness, brightness, (localPathslider) % 1.0)

            var circleList: MutableList<Circle> = mutableListOf()
            var circleListDark: MutableList<Circle> = mutableListOf()
            var circleListLight: MutableList<Circle> = mutableListOf()
            var imgList: MutableList<ColorBuffer> = mutableListOf()

            indices.forEach { (i, j) ->
//                circleList.add(
//                    Circle(
//                        GLOBAL.cellArray[i][j].position.x,
//                        GLOBAL.cellArray[i][j].position.y,
//                        GLOBAL.cellArray[i][j].renderW * shapeScaler
//                    )
//                )


                // I need localImgNum to interpolate between this brightness and the previous brightness.

                var localImgNum = GLOBAL.cellArray[i][j].brightness.map(
//                var localImgNum = GLOBAL.cellArray.flatten()[i*j].brightness.map(
                    0.0,
                    0.3,
                    0.0,
                    lidImgs.size.toDouble()
                ).toInt()


                // This seems to be the list that is being
                // updated
                // and
                // drawn
                // every frame.
                imgList.add(
                    lidImgs[localImgNum.toInt()]
                )
            }

            drawer.stroke = null
            drawer.pushTransforms()
            drawer.fill = ColorRGBa.LIGHT_GREEN
//            drawer.circles( circleListLight )
            drawer.circles( circleListDark )
            drawer.popTransforms()
//            println(GLOBAL.cellArray.flatten()[i].renderW)


            imgList.forEachIndexed{ i, n ->
                drawer.image(
                    n,
                    GLOBAL.cellArray.flatten()[i].position.x,
                    GLOBAL.cellArray.flatten()[i].position.y,
                    GLOBAL.cellWidth,
                    GLOBAL.cellHeight,
                )
            }

            // The problem is that rotatedImg needs to be used in place of currentImg in all instances.
//            drawer.image(rotatedImg.colorBuffer(0), 0.0, 0.0)
//            drawer.image(currentImg, 0.0, 0.0)
        }
    }
}

