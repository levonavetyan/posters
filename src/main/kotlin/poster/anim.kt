import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.isolated
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.noclear.NoClear

fun main() {

    application {

        configure {
            width = 800
            height = 800
        }

        program {
            class A : Animatable() {
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
                        animate("x", tx,  1000, Easing.CubicIn)
                        animate("y", ty, 1000, Easing.CubicIn)
                        animate("radius", tr,  speed /2, Easing.CubicIn)
                        animate("rotation", th , speed/2, Easing.CubicIn)

                    }
                }
            }

            val balls = mutableListOf<A>()

            for (i in 0 until 50) {
                balls.add(A())
            }

            val font = FontImageMap.fromUrl("file:data/fonts/IBMPlexMono-Regular.ttf", 32.0)

          //  extend(Screenshots())
          //  extend(NoClear())
            extend {
                for (a in balls) {
                    a.next()
                    //drawer.circle(a.x, a.y, a.radius)

                    //drawer.rectangle(a.x, a.y, a.radius, a.radius)
                    drawer.fontMap = font

                    drawer.isolated{
                        drawer.translate(a.x, a.y)
                        drawer.rotate(a.rotation)
                        drawer.translate(-a.x, -a.y)
                        drawer.text("Hello", a.x, a.y)

                    }

                }
            }
        }
    }

}