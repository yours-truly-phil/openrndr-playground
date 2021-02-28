import org.openrndr.application
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.math.smoothstep
import org.openrndr.shape.Rectangle

fun main() = application {
    configure {
        width = 1280
        height = 720
    }
    oliveProgram {
        val off = 20

        extend {
            drawer.clear(ColorRGBa.WHITE)
            drawer.translate(off.toDouble(), off.toDouble())

            (0 until width step off * 2).forEach { x ->
                (0 until height step off * 2).forEach { y ->
                    val f = smoothstep(0.0, 300.0, mouse.position.distanceTo(Vector2(x.toDouble(), y.toDouble())))
                    val rot = f * 90.0
                    val color = ColorHSVa(
                        255.0 * (x / width.toDouble()),
                        mouse.position.y / y.toDouble(),
                        (y - mouse.position.y) / height.toDouble()
                    )//, 1.0)
                    val color2 = ColorHSVa(255.0 * (x / width.toDouble()), f, y / height.toDouble())//, f)
                    drawer.stroke = color.toRGBa()
                    drawer.fill = color2.toRGBa()
                    drawer.rotate(rot)
                    val rect = Rectangle(-off.toDouble(), -off.toDouble(), off * 2.0, off * 2.0).scaled(f, f)
                    drawer.rectangle(rect)
                    drawer.rotate(-rot)
                    drawer.translate(0.0, off * 2.0)
                }
                drawer.translate(off * 2.0, -height * 1.0)
            }
        }
        mouse.dragged.listen {

        }
    }
}