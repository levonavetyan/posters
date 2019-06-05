package poster

import org.openrndr.animatable.Animatable
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.filter.blur.BoxBlur
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.filters.*
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
        val archive = File("data/archive/003")


        var lastChange = seconds

        //

        class Glitch : Animatable() {
            var separation = 0.0
        }


        val glitch = Glitch()

        archive.listFiles().forEach {
            if (it.extension == "jpg" || it.extension == "png") {
                val image = loadImage(it)
                images.add(image)
            }
        }

        val poster = compose {

            layer {

                blend(Add())

//                post(BoxBlur()){
//                    this.window = mouse.position.y.toInt()
//                }

                //mouse.scrolled.listen() {

                    post(Separate()) {
                        redShift = Vector2(0.0, 0.0) * glitch.separation
                        greenShift = Vector2(1.0 / 40.0, 0.0) * glitch.separation
                        blueShift = Vector2(0.0, 1.0 / 40.0) * glitch.separation

                        //  }
                    }

//                post(Mosaic()) {
//                    xSteps = 320
//                    ySteps = 322
//
//
//                }


                draw {




                    if (seconds - lastChange > 1.0) {


                      //  println("I am shuffling the images")
                        lastChange = seconds
                        images.shuffle()
                    }

                    //drawer.fill = ColorRGBa.PINK
                    //
                    // drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK)

                     // drawer.scale(1.0)



                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {
                    drawer.image(images[index], x/600.0, y/800.0)

                            index ++



                        }
                    }

                  //  }





                }





            }


            layer {


                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/Rumeur/rumeur.otf", 32.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    drawer.translate(10.0, 10.0)
                    // drawer.text("Adele - Rolling in the deep", 120.0, 300.0)
                }


            }
            layer {


                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/Rumeur/rumeur.otf", 46.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    val date = LocalDateTime.now()
                    drawer.translate(10.0, 10.0)
                    drawer.text("Imagine Dragons ", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0)
                    drawer.text("Thunder", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)

                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            glitch.updateAnimation()

            if (!glitch.hasAnimations()) {

                glitch.animate("separation", 1.0, 100)
                glitch.complete()
                glitch.animate("separation", 0.0, 1000)

            }

            poster.draw(drawer)


        }
    }

}

