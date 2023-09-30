import GLOBAL.cellHeight
import GLOBAL.cellWidth
import GLOBAL.sceneInterval
import GLOBAL.shapeScaler
import classes.Cell
import classes.Flag
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.loadImage
import org.openrndr.extra.color.presets.LIGHT_GREEN
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
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
        width = 426
        height = 240
        hideWindowDecorations = true
        windowAlwaysOnTop = true
        windowTransparent = true
        position = IntVector2(100,100)
    }

    oliveProgram {
        GLOBAL.width = width
        GLOBAL.height = height
        val imgCount = 320
        GLOBAL.brightnessThreshold = 0.0
        GLOBAL.shapeScaler = 1.0

        var thisClock: Double
        // I think adjusting clockDiv adjusts the framerate
//        val clockDiv = 0.25  // clockDiv of 0.05 means 20 frames between scenes I think
        val clockDiv = 0.25  // clockDiv of 0.05 means 20 frames between scenes I think
        val framesBtwnScenes = ((1.0 / clockDiv)) // this should mean how many frames between new scene
        // I think adjusting sceneInterval adjusts the sample rate
        GLOBAL.sceneInterval = 1  // this should mean how many intervals should pass between drawing a scene
        val framesBetweenSceneIntervals = framesBtwnScenes * sceneInterval

        // "fire scene interval" when newSceneCounter >= sceneInterval
        val ratio = width.toDouble() / height.toDouble()
        val imgs = mutableListOf<ColorBuffer>()
        for(i in 1 until imgCount) {
            imgs.add(loadImage("data/images/frames5/$i.png"))
        }

        val imageFiles = File("data/images/lidFrames").listFiles { _, name -> name.endsWith(".png") }?.sorted()
        val lidImgs: List<ColorBuffer> = imageFiles!!.map { loadImage(it) }

        val gridWidth = 60.0      // Number of grid units in width
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

        extend {
            thisClock = frameCount * clockDiv
            currentImg = imgs[(thisClock).toInt() % imgs.size]
            drawer.clear(ColorRGBa.BLACK)
//            drawer.image(currentImg, 0.0, 0.0)

            var idIncrementor = 0
            indices.forEach { (i, j) ->
                currentColor = currentImg.shadow.read(
                    i * cellWidth.toInt(), j * cellHeight.toInt()
                )
                val brightness =
                    0.2126 * currentColor.r
                + 0.7152 * currentColor.g
                + 0.0722 * currentColor.b

                GLOBAL.cellArray[i][j].id = idIncrementor++.toString() //   i can move this to the main block
                GLOBAL.cellArray[i][j].brightness = brightness
                GLOBAL.cellArray[i][j].color = currentColor.toSRGB()
                GLOBAL.cellArray[i][j].position = Vector2((i * cellWidth), (j * cellHeight))
            }
            testFlag.check(thisClock, sceneInterval)


            var circleList: MutableList<Circle> = mutableListOf()
            var circleListDark: MutableList<Circle> = mutableListOf()
            var circleListLight: MutableList<Circle> = mutableListOf()

            indices.forEach { (i, j) ->
                circleList.add(
                    Circle(
                        GLOBAL.cellArray[i][j].position.x,
                        GLOBAL.cellArray[i][j].position.y,
                        GLOBAL.cellArray[i][j].renderW * shapeScaler
                    )
                )
                if(GLOBAL.cellArray[i][j].brightness > 0.185){
                    circleListLight.add(
                        Circle(
                            GLOBAL.cellArray[i][j].position.x,
                            GLOBAL.cellArray[i][j].position.y,
                            GLOBAL.cellArray[i][j].renderW * shapeScaler
                        )
                    )
                } else {
                    circleListDark.add(
                        Circle(
                            GLOBAL.cellArray[i][j].position.x,
                            GLOBAL.cellArray[i][j].position.y,
                            GLOBAL.cellArray[i][j].renderW * shapeScaler
                        )
                    )
                }
            }

            drawer.stroke = null
            drawer.pushTransforms()
            drawer.fill = ColorRGBa.LIGHT_GREEN
//            drawer.circles( circleListLight )
            drawer.circles( circleListDark )
            drawer.popTransforms()

            drawer.fill = null
            drawer.stroke = ColorRGBa.WHITE
            drawer.strokeWeight = 0.5
            drawer.rectangle(0.0, 0.0, width.toDouble(), height.toDouble())
            drawer.stroke = null

            val increm = (frameCount*0.5).toInt()
                drawer.image(
                    lidImgs[increm % lidImgs.size],
                    0.0,
                    0.0,
                    drawer.bounds.height,
                    drawer.bounds.height
                )
            println("testing")
        }
    }
}

