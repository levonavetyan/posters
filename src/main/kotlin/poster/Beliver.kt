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
        val archive = File("data/archive/004")

        class Anim : Animatable() {
            var line = 0.0
            var dr = 0.0
            var db = 0.0
            var dg = 0.0
            var lr = 0.0
            var lg = 0.0
            var lb = 0.0
        }

        class Letter(val letter: String, var x: Double, var y: Double, val homeX: Double = x, val homeY: Double = y, var opacity:Double = 0.0) : Animatable()

        val letters = mutableListOf<Letter>()

        var x = 0.0
        val font = FontImageMap.fromUrl("file:data/fonts/OpenSansCondensed-Bold.ttf", 144.0)

        for (l in "BELIEVER") {
            drawer.fontMap = font

            val w = Writer(drawer)
            val letterWidth = w.textWidth(l.toString())

            letters.add(Letter(l.toString(), x, height / 2.0))

            x += letterWidth
            x += 30.0


        }


        val anim = Anim()

        var lastChange = seconds

        //

        var index = 0
        archive.listFiles().forEach {
            if (it.extension == "jpg" || it.extension == "png") {
                val image = loadImage(it)
                images.add(image)
            }
        }

        val poster = compose {

            layer {

                blend(Add())

                post(Threshold()) {
                    dark = ColorRGBa(anim.dr, anim.dg, anim.db)
                    light = ColorRGBa(anim.lr, anim.lg, anim.lb)

                    threshold = anim.line

                    //  }
                }
//                  post(BoxBlur()){
//                   this.window = mouse.position.y.toInt()
//                  }


                draw {


                    if (seconds - lastChange > 1.0) {


                        //  println("I am shuffling the images")
                        lastChange = seconds


                    }

                    //drawer.fill = ColorRGBa.PINK
                    //
                    // drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK)

                    drawer.scale(1.0)





                    drawer.image(images[index%images.size])


                }


            }


            layer {


                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/OpenSansCondensed-Bold.ttf", 32.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    drawer.translate(10.0, 10.0)
                    // drawer.text("Adele - Rolling in the deep", 120.0, 300.0)
                }
            }
            layer {


                draw {
                    val font = FontImageMap.fromUrl("file:data/fonts/OpenSansCondensed-Bold.ttf", 144.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    val date = LocalDateTime.now()
                    drawer.translate(10.0, 10.0)
                    //drawer.text("BELIEVER", width/2.0, height/2.0)

                    for (letter in letters) {
                        drawer.fill = ColorRGBa.WHITE.opacify(letter.opacity)
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
            anim.updateAnimation()

            for (letter in letters) {
                letter.updateAnimation()


            }

            if (!anim.hasAnimations()) {
                val duration = 4000L //(Math.random() * 800 + 1200).toLong()
                anim.animate("line", 0.0, 0, Easing.CubicIn)
                anim.complete()
                anim.animate("line", 0.5, duration / 2, Easing.CubicIn)


                var darkColor = ColorHSVa(Math.random() * 360.0, Math.random() * 0.3 + 0.4, Math.random() * 0.5 + 0.5).toRGBa()
                var lightColor = darkColor.toHSVa().shiftHue((Math.random() - 0.5) * 40.0).scaleSaturation(0.7).scaleValue(1.1).toRGBa()


                anim.animate("dr", darkColor.r, duration)
                anim.animate("dg", darkColor.g, duration)
                anim.animate("db", darkColor.b, duration)
                anim.animate("lr", lightColor.r, duration)
                anim.animate("lg", lightColor.g, duration)
                anim.animate("lb", lightColor.b, duration)


                var letterIndex = 0
                for (letter in letters) {

                    letter.cancel()

                    //letter.animate("x", Math.random()*width,  1000, Easing.CubicInOut)
                    val d = Vector2(Math.random() * width, Math.random() * height) - Vector2(width / 2.0, height / 2.0)
                    val dn = d.normalized
                    val p = dn * 500.0 + Vector2(width / 2.0, height / 2.0)





                    letter.animate("x", letter.homeX, 0)
                    letter.animate("y", letter.homeY, 0)
                    letter.animate("opacity", 0.0, 0)



                    letter.delay(letterIndex*100L)
                    letterIndex++
                    letter.animate("opacity", 1.0, 400)

                    //letter.animate("x", p.x, duration-1000, Easing.CubicOut)
                    //letter.animate("y", p.y, duration-1000, Easing.CubicOut)

                    letter.animate("x", Math.random() * width, duration - 1000, Easing.CubicInOut)

                    letter.animate("y", Math.random() * height, duration - 1000, Easing.CubicInOut)
                    letter.animate("dummy", 1.0, 1000000000, Easing.CubicInOut)


                }

                index++

            }
            anim.updateAnimation()

            poster.draw(drawer)


        }
    }

}
