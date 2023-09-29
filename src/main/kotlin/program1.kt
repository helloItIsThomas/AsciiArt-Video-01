//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.draw.*
//import org.openrndr.extra.olive.oliveProgram
//import org.openrndr.math.Vector2
//import org.openrndr.math.map
//
//fun main() = application {
//    configure {
//        width = 426
//        height = 240
//    }
//
//    oliveProgram {
//        val imgs = mutableListOf<ColorBuffer>()
//        for(i in 1 until 550) {
//            imgs.add(loadImage("data/images/frames2/img$i.png"))
//        }
//        val img = loadImage("data/images/grid.png")
//        val gridWidth = 20 // Number of grid units in width
//        val gridHeight = gridWidth // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//        val brightnessValues = Array(gridWidth) { Array(gridHeight) { 0.0 } }
//        var currentImg: ColorBuffer
//        var currentColor = ColorRGBa.WHITE
//        img.shadow.download()
//        imgs.forEach{it.shadow.download()}
//
//        extend {
//            currentImg = imgs[(frameCount*0.25).toInt() % imgs.size]
//
//            // Read the color of the first pixel of every cell
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    currentColor = currentImg.shadow.read(
//                        i * cellWidth, j * cellHeight
//                    )
//                    currentColor = currentColor
//                    val brightness = 0.2126 * currentColor.r + 0.7152 * currentColor.g + 0.0722 * currentColor.b
//                    brightnessValues[i][j] = brightness
//                }
//            }
//
////            drawer.image(currentImg)
//
//            drawer.fill = currentColor
//
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    var tempW = brightnessValues[i][j].map(
//                        0.0,
//                        1.0,
//                        0.0,
//                        cellWidth.toDouble()
//                    )
////                    drawer.rectangle(i * cellWidth.toDouble(), j * cellHeight.toDouble(), cellWidth.toDouble(), cellHeight.toDouble())
//                    drawer.pushTransforms()
////                    drawer.translate(cellWidth * 0.5, cellHeight * 0.5)
////                    drawer.rectangle(
////                        (i * cellWidth.toDouble()),
////                        (j * cellHeight.toDouble()),
////                        tempW,
//////                        cellWidth.toDouble(),
////                        cellHeight.toDouble()
////                    )
//                    drawer.circle(
//                        (i * cellWidth.toDouble()),
//                        (j * cellHeight.toDouble()),
//                        tempW * 1.0
//                    )
//                    drawer.popTransforms()
//                }
//            }
//        }
//    }
//}
