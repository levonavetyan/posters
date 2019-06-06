package poster

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.filter.blur.BoxBlur
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



        class Letter(val letter: String, var x: Double, var y: Double, val homeX: Double = x, val homeY: Double = y, var opacity:Double = 0.0) : Animatable()

        val letters = mutableListOf<Letter>()

        var x = 0.0
        val font = FontImageMap.fromUrl("file:data/fonts/chequered-ink_thundercover/electrical.ttf", 43.0)

        for (l in "Thunder") {
            drawer.fontMap = font

            val w = Writer(drawer)
            val letterWidth = w.textWidth(l.toString())

            letters.add(Letter(l.toString(), x, height / 2.0))

            x += letterWidth
            x += 30.0


        }

       // glitch.separation
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

                   // drawer.image(images[index%images.size])


                    //drawer.fill = ColorRGBa.PINK
                    //
                    // drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK)

                     // drawer.scale(1.0)



                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {
                    drawer.image(images[index%images.size], x/600.0, y/800.0)

                            index ++



                        }
                    }

                  //  }





                }





            }


            layer {


                post(Separate()) {
                    redShift = Vector2(0.0, 0.0) * glitch.separation
                    greenShift = Vector2(1.0 / 40.0, 0.0) * glitch.separation
                    blueShift = Vector2(0.0, 1.0 / 40.0) * glitch.separation

                    //  }
                }
                post(VerticalStepWaves()) {
                    steps = (4 * glitch.separation).toInt()
                    phase = seconds * 10.0
                    amplitude = 0.01
                    period =  Math.PI * 4.0 * glitch.separation

                    //  }
                }



                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/chequered-ink_thundercover/electrical.ttf", 43.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    val date = LocalDateTime.now()
                    drawer.translate(40.0 , 250.0  )
                  //  drawer.text("Imagine Dragons ", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0)
                  //  drawer.text("Thunder", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)
                    for (letter in letters) {
                        drawer.fill = ColorRGBa.WHITE.opacify(letter.opacity)
                        drawer.text(letter.letter , letter.x , letter.y)

                    }
                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            glitch.updateAnimation()


            for (letter in letters) {
                letter.updateAnimation()


            }

            if (!glitch.hasAnimations()) {
                val duration = 4000L //(Math.random() * 800 + 1200).toLong()
                glitch.animate("separation", 1.0, 100)
                glitch.complete()
                glitch.animate("separation", 0.0, 1000)


                var letterIndex = 0
                for (letter in letters) {


                    letter.cancel()

                    //letter.animate("x", Math.random()*width,  1000, Easing.CubicInOut)
                   // val d = Vector2(Math.random() * width, Math.random() * height) - Vector2(width / 2.0, height / 2.0)
                  //  val dn = d.normalized
                  //  val p = dn * 500.0 + Vector2(width / 2.0, height / 2.0)





                 //   letter.animate("x", letter.homeX, 0)
                  //  letter.animate("y", letter.homeY, 0)
                  //  letter.animate("opacity", 0.0, 0)



                    letter.delay(letterIndex*100L)
                    letterIndex++
                    letter.animate("opacity", 1.0, 400)



                    //letter.animate("x", p.x, duration-1000, Easing.CubicOut)
                    //letter.animate("y", p.y, duration-1000, Easing.CubicOut)

                   // letter.animate("x", Math.random() * width, duration - 1000, Easing.CubicInOut)

                   letter.animate("y", Math.random() * height, duration - 1000, Easing.CubicInOut)
                   letter.animate("dummy", 1.0, 1000000000, Easing.CubicInOut)


                }

            }

            poster.draw(drawer)


        }
    }

}

