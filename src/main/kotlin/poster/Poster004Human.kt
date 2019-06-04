package poster

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.shape.Rectangle
import org.openrndr.workshop.toolkit.filters.ZoomMosaic
import java.io.File
import java.time.LocalDateTime

fun main() = application {


    configure {
        width = 600
        height = 800
    }

    program {
       // val image = loadImage("file:data/images/Adele Photoshop/adele10.jpg")

        val images = mutableListOf<ColorBuffer>()
        val archive = File("data/archive/002")



        var lastChange = seconds

        //

        archive.listFiles().forEach {
            if (it.extension == "jpg" || it.extension == "png") {
                val image = loadImage(it)
                images.add(image)
            }
        }

        val poster = compose {

            layer {

                blend(Add())


                draw {


                    if (seconds - lastChange > 1.0) {


                        println("I am shuffling the images")
                        lastChange = seconds
                        images.shuffle()
                    }





                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {

                            drawer.image(images[index],
                                    Rectangle(x * 300.0, y * 400.0, 300.0, 400.0),
                                    Rectangle(x * 300.0, y * 400.0, 300.0, 400.0))
                            index ++
                        }

                    }

                }

            }

        }

        extend {
            poster.draw(drawer)


        }
    }

}

