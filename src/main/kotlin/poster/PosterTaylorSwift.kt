
package poster

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.filter.blur.BoxBlur
import org.openrndr.filter.blur.DropShadow
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
        //val image = loadImage("file:data/images/Adele Photoshop/adele10.jpg")

        val images = mutableListOf<ColorBuffer>()
        val archive = File("data/archive/004/Taylor")


        var lastChange = seconds

        //

        archive.listFiles().forEach {
            if (it.extension == "jpg" || it.extension == "png") {
                val image = loadImage(it)
                images.add(image)
            }
        }
        class Zoom: Animatable(){
            var zooming = 0.0
        }

        val zoom = Zoom()
        val poster = compose {


            layer {

                blend(Add())


//                  post(BoxBlur()){
//                   this.window = mouse.position.y.toInt()
//                  }


                post(StepWaves()) {

                    phase = 0.0* zoom.zooming
                    amplitude = 0.1* zoom.zooming
                    period = Math.PI * 4.0 * zoom.zooming

                }


                draw {




                    if (seconds - lastChange > 1.0) {


                      //  println("I am shuffling the images")
                        lastChange = seconds
                    }

                    //drawer.fill = ColorRGBa.PINK
                    //
                    // drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK)

                    drawer.scale(1.0)


                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {

                            drawer.image(images[index], x*600.0, y*800.0)
                            index ++
                        }


                    }





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
                //    drawer.text("Adele ", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0)
                //    drawer.text("Rolling in the deep", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)

                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            zoom.updateAnimation()

            if (!zoom.hasAnimations()) {

                zoom.animate("zooming", 1.0, 100, Easing.CubicIn)
                zoom.complete()
                zoom.animate("zooming", 0.0, 1000, Easing.CubicIn)
                images.shuffle()

            }
            poster.draw(drawer)


        }
    }

}

