import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.grayscale
import org.openrndr.draw.isolated
import org.openrndr.draw.tint
import org.openrndr.extra.noclear.NoClear
import org.openrndr.ffmpeg.FFMPEGVideoPlayer

fun main() {
    application {
        configure {
            height = 1920
            width = 1400
        }
        program {
            val videoPlayer = FFMPEGVideoPlayer.fromDevice()
            videoPlayer.start()

            var imagecolor = ColorRGBa.WHITE
            var scale = 1.0

            var clear = false


            class Brush: Animatable() {
                var scale = 1.0
            }

            class Rotate:Animatable(){
            var rotate = 1.0

            }

            var rotate = Rotate()
            var brush = Brush()

            mouse.clicked.listen(){
                imagecolor = ColorRGBa(Math.random(), Math.random(), Math.random())


            }

            keyboard.character.listen{
                if(it.character == 'a') {
                scale = Math.random()
            }

                if(it.character == 'l') {
                 rotate.rotate = Math.random()*360.0
                }

                if(it.character == 's'){
                    clear = true
                }

                if(it.character == 'g'){
                    rotate.cancel()
                    rotate.animate("rotate", Math.random()*360.0, 1000, Easing.CubicOut)
                }



                if(it.character == 'd'){
                    brush.cancel()
                    brush.animate("scale", Math.random(), 1000, Easing.CubicOut)
                }

            }

            extend(NoClear())
            extend {
                //drawer.background(ColorRGBa.BLACK)
                brush.updateAnimation()
                rotate.updateAnimation()
                videoPlayer.next()
                if (clear){
                    drawer.background(ColorRGBa.BLACK)
                    clear = false
                }

                drawer.translate(mouse.position)


                drawer.isolated {
                    drawer.drawStyle.colorMatrix = tint(imagecolor) * grayscale()

                    //drawer.translate(videoPlayer.width/2.0, videoPlayer.height/2.0)

                    drawer.scale(brush.scale)
                    drawer.rotate(rotate.rotate)
                    drawer.translate(-videoPlayer.width/2.0, -videoPlayer.height/2.0)

                    videoPlayer.draw(drawer)


//
//
//
//                val r = Math.cos(seconds)* 0.5 + 0.5
//                val g = Math.sin(seconds)* 0.5 + 0.5
//                val b = Math.sin(seconds * 1.32)* 0.5 + 0.5
//


                }
            }
        }
    }
}