package poster

import org.jetbrains.kotlin.backend.common.onlyIf
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
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
        val archive = File("data/archive/002/done")
        val font = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Regular.ttf", 92.0)

        class Light: Animatable(){
            var rgb = 0.0

        }
        var light = Light()

        class Tumo : Animatable() {
            var coach = 0.0
            var x: Double = 0.0
            var y: Double = 0.0
            var radius: Double = 0.0
            var rotation = 0.0
            fun next() {
                updateAnimation()
                if (!hasAnimations()) {

                    var speed = (Math.random() * 1000 + 1000).toLong()

                    var tx = Math.random() * width
                    var ty = Math.random() * height
                    val tr = Math.random() * 100.0
                    var th = Math.random() * 180.0
                    animate("x", tx,  1000, Easing.CubicIn )
                    animate("y", ty, 1000  , Easing.CubicIn)
                    animate("radius", tr,  speed /2 , Easing.CubicIn )
                    animate("rotation", th , speed/2, Easing.CubicIn)

                }
            }
        }
        val balls = mutableListOf<Tumo>()

        for (i in 0 until 1) {
            balls.add(Tumo())
        }
       // var tumo = Tumo()
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


//                  post(BoxBlur()){
//                   this.window = mouse.position.y.toInt()
//                  }


                post(VerticalStepWaves()) {
                    this.amplitude = 0.1
                    this.phase = seconds
                    this.period = 10.0


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


                draw {
                    for (a in balls) {
                        a.next()
                        //drawer.circle(a.x, a.y, a.radius)

                        //drawer.rectangle(a.x, a.y, a.radius, a.radius)
                        drawer.fontMap = font

                        drawer.isolated{
                            drawer.translate(a.x, a.y)
                            drawer.rotate(a.rotation)
                            drawer.translate(-a.x, -a.y)
                            drawer.text("Shape of You", a.x, a.y)

                        }

                    }

                    val font = FontImageMap.fromUrl("file:data/fonts/Rumeur/rumeur.otf", 46.0)
                    drawer.fontMap = font
                    drawer.fill = ColorRGBa.WHITE
                    val date = LocalDateTime.now()
                    drawer.translate(-210.0, 10.0  )
                   // drawer.text("Ed Sheeran ", Math.cos(seconds) * width / 2.0 * tumo.coach + width / 2.0, Math.sin(0.5 * seconds) * height  / 2.0 + height / 2.0)
                  //  drawer.text("Shape of you", Math.cos(seconds) * width / 2.0 * tumo.coach  + width / 2.0, Math.sin(0.5 * seconds) * height / 2.0 + height / 2.0 + 45.0)

                    //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                    // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {

            light.updateAnimation()
            poster.draw(drawer)


        }
    }

}
