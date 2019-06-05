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


        val anim = Anim()

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


                    var index = 0
                    for (y in 0 until 2) {
                        for (x in 0 until 2) {

                            drawer.image(images[index], x * 600.0, y * 800.0)
                            index++
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
//                    drawer.text("Ed Sheeran ", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0)
//                    drawer.text("Shape of you", Math.cos(seconds) * width / 2.0 + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)

                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            anim.updateAnimation()
            if (!anim.hasAnimations()) {
                val duration = (Math.random() * 800 + 1200).toLong()
                anim.animate("line", 0.0, 0, Easing.CubicIn)
                anim.complete()
                anim.animate("line", 0.5, duration / 2, Easing.CubicIn)



                var darkColor = ColorHSVa(Math.random()*360.0, Math.random()*0.3+ 0.4, Math.random()*0.5+0.5).toRGBa()
                var lightColor = darkColor.toHSVa().shiftHue((Math.random()-0.5)*40.0 ).scaleSaturation(0.7).scaleValue(1.1).toRGBa()


                anim.animate("dr",darkColor.r, duration)
                anim.animate("dg",darkColor.g, duration)
                anim.animate("db",darkColor.b, duration)
                anim.animate("lr", lightColor.r, duration)
                anim.animate("lg", lightColor.g, duration)
                anim.animate("lb", lightColor.b, duration)

                images.shuffle()

            }
            anim.updateAnimation()

            poster.draw(drawer)


        }
    }

}
