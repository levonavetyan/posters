package poster

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.text.Writer
import org.openrndr.workshop.toolkit.filters.GradientMap
import org.openrndr.workshop.toolkit.filters.Threshold
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


        class Narek: Animatable(){
            var humanism = 0.0
        }
        var narek = Narek()

        var lastChange = seconds

        //

        archive.listFiles().forEach {
            if (it.extension == "jpg" || it.extension == "png") {
                val image = loadImage(it)
                images.add(image)
            }
        }

        class Letter(val letter: String, var x: Double, var y: Double, val homeX: Double = x, val homeY: Double = y, var opacity:Double = 0.0) : Animatable()

        val letters = mutableListOf<Letter>()

        var x = 0.0
        val font = FontImageMap.fromUrl("file:data/fonts/Jost/Jost-600-Semi.ttf", 43.0)

        for (l in "HUMAN") {
            drawer.fontMap = font

            val w = Writer(drawer)
            val letterWidth = w.textWidth(l.toString())

            letters.add(Letter(l.toString(), x, height / 2.0))

            x += letterWidth
            x += 60.0


        }

        val poster = compose {

            layer {

                blend(Add())


                draw {


                    if (seconds - lastChange > 1.0) {


                      //  println("I am shuffling the images")
                        lastChange = seconds
                        images.shuffle()
                    }





                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {

                            drawer.image(images[index],
                                    Rectangle(x * 300.0, y * 400.0, 300.0, 400.0),
                                    Rectangle(x * 300.0, y * 400.0, 300.0, 400.0))
                            index++

                        }

                    }

                }

            }

            layer {

                post(GradientMap()) {



                    //  }
                }
                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/Jost/Jost-600-Semi.ttf", 144.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    val date = LocalDateTime.now()
                    drawer.translate(100.0, 250.0 )
                    //drawer.text("BELIEVER", width/2.0, height/2.0)

                    for (letter in letters) {
                        drawer.fill = ColorRGBa.WHITE.opacify(letter.opacity )
                        drawer.text(letter.letter, letter.x, letter.y)

                    }

//                    drawer.text("Ed Sheeran ", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0)
//                    drawer.text("Shape of you", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)

                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }

        }

        extend {

            narek.updateAnimation()

            for (letter in letters) {
                letter.updateAnimation()


            }
            if (!narek.hasAnimations()) {
                val duration = 4000L //(Math.random() * 800 + 1200).toLong()
                narek.animate("humanism", 0.0, 0, Easing.CubicIn)
                narek.complete()
                narek.animate("humanism", 0.5, duration / 2, Easing.CubicIn)



                var letterIndex = 0
                for (letter in letters) {

                    letter.cancel()

                    //letter.animate("x", Math.random()*width,  1000, Easing.CubicInOut)
                    val d = Vector2(Math.random() * width, Math.random() * height) - Vector2(width / 2.0, height / 2.0)
                    val dn = d.normalized
                    val p = dn * 500.0 + Vector2(width / 2.0, height / 2.0)






                    letter.animate("x", p.x, 0, Easing.CubicOut)
                    letter.animate("y", p.y, 0, Easing.CubicOut)

//                    letter.animate("x", Math.random() * width, duration - 1000, Easing.CubicInOut)
//
//                   letter.animate("y", Math.random() * height, duration - 1000, Easing.CubicInOut)
//                    letter.animate("dummy", 1.0, 1000000000, Easing.CubicInOut)




                    letter.animate("x", letter.homeX, 1000, Easing.CubicOut)
                    letter.animate("y", letter.homeY, 1000, Easing.CubicOut)
                    letter.animate("opacity", 0.0, 0)


                    letter.delay(letterIndex*100L)
                    letterIndex++
                    letter.animate("opacity", 1.0, 400)





                }
                //index++

            }
            narek.updateAnimation()


            poster.draw(drawer)


        }
    }

}

