import org.openrndr.WindowMultisample
import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun main() = application {
    configure {
        width = 1280
        height = 720
        multisample = WindowMultisample.SampleCount(4)
    }
    oliveProgram {
        var rot = 0.0
        extend {
            drawer.clear(ColorRGBa.WHITE)

            drawer.translate(width / 2.0, height / 2.0)
            drawer.stroke = ColorRGBa.TRANSPARENT
            drawer.fill = ColorRGBa.BLACK

            var radius = 1000.0
            repeat(30) {
                val rectWidth = radiusToWidth(radius)
                repeat(1) {
                    drawer.fill = ColorRGBa.BLACK
                    drawer.circle(0.0, 0.0, radius)
                    drawer.fill = ColorHSVa(256 * (cos(seconds) + 1.0) / 2.0,  (sin(seconds) + 1.0)/2.0, 1.0).toRGBa()
                    drawer.rectangle(-rectWidth / 2.0, -rectWidth / 2.0, rectWidth, rectWidth)
                    drawer.rotate(seconds * rot)
                }
                radius = rectWidth / 2.0
            }
        }

        mouse.dragged.listen {
            val rate = 0.1
            rot += it.dragDisplacement.x * rate
        }
    }
}

fun radiusToWidth(radius: Double): Double = sqrt(radius * radius * 2)