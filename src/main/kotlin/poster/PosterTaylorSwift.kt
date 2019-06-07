
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
import org.openrndr.extra.noclear.NoClear
import org.openrndr.filter.blend.Add
import org.openrndr.filter.blend.Multiply
import org.openrndr.filter.blur.BoxBlur
import org.openrndr.filter.blur.DropShadow
import org.openrndr.math.Vector2
import org.openrndr.text.Writer
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
            var taylor = 0.0
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
                        images.shuffle()

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

//                clearColor = null

                post(DropShadow())
             //  blend(Multiply())
                draw {
//                    drawer.fill = ColorRGBa.WHITE.opacify(0.1)
//                    drawer.rectangle(0.0, 0.0, width*1.0, height*1.0)


                    val font = FontImageMap.fromUrl("file:data/fonts/youmurdererbb_reg.otf", 140.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa(99.0/255.0, 19.0/255.0, 28.0/255.0).shade(1.1).opacify(0.8)
                    val date = LocalDateTime.now()
                    drawer.translate(10.0, 10.0)


                    val words = "Look what you made me do".split(" ")

                    val wordIntervals = listOf(0.0, 0.25, 0.5, 0.75, 1.1, 1.25)

                    val wordCount = (seconds*3.0).toInt() % (words.size)

                    val time = seconds % 2.0

                    var y = 200.0
                    for (i in 0 until words.size) {

                        val w = Writer(drawer)
                        val tw = w.textWidth(words[i])
                        if (wordIntervals[i] <= time)
                        drawer.text(words[i], width/2.0-tw/2.0, y)
                        y+=80.0
                    }



                    //drawer.text("Look what you  ", 20.0 / (zoom.zooming + 204), 500.0/  zoom.zooming)
                    //drawer.text("made me do ", 230.0/ zoom.taylor, 550.0/ zoom.taylor)






                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            zoom.updateAnimation()


            if (!zoom.hasAnimations()) {

                zoom.animate("zooming", 1.0, 150, Easing.CubicIn)
                zoom.complete()
                zoom.animate("zooming", 0.0, 1500, Easing.CubicIn)

                zoom.animate("taylor", 1.0, 200, Easing.CubicIn)
                zoom.complete()
                zoom.animate("taylor", 0.0, 2000, Easing.CubicIn)

            }
            poster.draw(drawer)


        }
    }

}

