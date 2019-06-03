package examples

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.compositor.*
import org.openrndr.filter.blend.Add
import org.openrndr.filter.blur.BoxBlur
import org.openrndr.filter.blur.DropShadow
import org.openrndr.math.Vector2
import org.openrndr.workshop.toolkit.filters.*
import java.time.LocalDateTime

fun main() = application {



    configure {
        width = 600
        height = 800
    }

    program {
        val image = loadImage("file:data/images/Adele Photoshop/adele10.jpg")
        val image1 = loadImage("file:data/images/Adele Photoshop/cups.jpg")

        val poster = compose {

            layer {

                blend(Add())


//                  post(BoxBlur()){
//                   this.window = mouse.position.y.toInt()
//                  }


                post(ZoomMosaic()){
                    this.xSteps = 32
                    this.ySteps = 32
                    this.scale = Math.cos(seconds ) * 2


                }


                draw {
                    //drawer.fill = ColorRGBa.PINK
                    //
                    // drawer.drawStyle.colorMatrix = tint(ColorRGBa.PINK)
                    drawer.image(image)



                }



                    mouse.clicked.listen(){


                        draw{
                            drawer.image(image1)

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
                    drawer.text("Adele ", Math.cos(seconds)*width/2.0+width/2.0, Math.sin(0.5*seconds)*height/2.0 + height/2.0)
                    drawer.text("Rolling in the deep", Math.cos(seconds)*width/2.0+width/2.0, Math.sin(0.5*seconds)*height/2.0 + height/2.0 + 45.0)

                  //  drawer.text("${date.month.name} ${date.dayOfMonth}", 0.0, 280.0)
                   // drawer.text("${date.year}", 0.0, 360.0)
                }
            }
        }

        extend {
            poster.draw(drawer)



        }
    }

}

