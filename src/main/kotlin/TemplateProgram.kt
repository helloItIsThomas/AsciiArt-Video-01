//import org.openrndr.application
//import org.openrndr.color.ColorRGBa
//import org.openrndr.draw.*
//import org.openrndr.math.Vector2
//import org.openrndr.math.map
//
//fun main() = application {
//    configure {
//        width = 768
//        height = 576
//    }
//
//    program {
//        val image = loadImage("data/images/grid.png")
//        val gridWidth = 10 // Number of grid units in width
//        val gridHeight = 10 // Number of grid units in height
//
//        val cellWidth = width / gridWidth
//        val cellHeight = height / gridHeight
//
//        val brightnessValues = Array(gridWidth) { Array(gridHeight) { 0.0 } }
//
//        extend {
//            drawer.image(image)
//            // Read the color of the first pixel of every cell
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    var color = image.shadow.read(
//                        (i * cellWidth), (j * cellHeight)
//                    )
//                    val brightness = 0.2126 * color.r + 0.7152 * color.g + 0.0722 * color.b
//                    brightnessValues[i][j] = brightness
//                }
//            }
//
//            // Now you have the brightness values for every cell in your grid
//            // Here, we're simply drawing the image and the grid
//            drawer.image(image)
//
//            // Let's draw the grid and print the brightness value for demonstration
////            drawer.stroke = ColorRGBa.WHITE
//            drawer.fill = ColorRGBa.BLUE
//            drawer.stroke = ColorRGBa.BLACK
//
//            for (i in 0 until gridWidth) {
//                for (j in 0 until gridHeight) {
//                    var tempW = brightnessValues[i][j].map(
//                        0.0,
//                        255.0,
//                        0.0,
//                        cellWidth.toDouble()
//                    )
////                    drawer.rectangle(i * cellWidth.toDouble(), j * cellHeight.toDouble(), cellWidth.toDouble(), cellHeight.toDouble())
//                    drawer.rectangle(i * cellWidth.toDouble(), j * cellHeight.toDouble(), tempW, cellHeight.toDouble())
//                }
//            }
//        }
//    }
//}
