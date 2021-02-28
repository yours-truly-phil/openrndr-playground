import org.openrndr.WindowMultisample
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.midi.MidiDeviceDescription
import org.openrndr.extra.midi.MidiTransceiver
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun main() = application {
    configure {
        width = 1280
        height = 720
        multisample = WindowMultisample.SampleCount(4)
    }
    oliveProgram {
        val points = initPoints(Vector2(width.toDouble(), height.toDouble()), 100)
        MidiDeviceDescription.list().forEach {
            println("name: '${it.name}' vendor: '${it.vendor}'")
        }
        val controller = MidiTransceiver.fromDeviceVendor("Arturia MiniLab mkII", "Arturia")
        var curAnim = 0.0
        var size = 10.0
        controller.noteOn.listen {
            size = 10.0 + it.velocity.toDouble() / 3.0
            curAnim = 1.0
        }
        extend {
            drawer.clear(ColorRGBa.BLACK)
            drawer.fill = ColorRGBa.GREEN
            points.forEach {
                if (it.pos.x + it.vel.x < 0.0 || it.pos.x + it.vel.x > width) it.vel.x *= -1
                if (it.pos.y + it.vel.y < 0.0 || it.pos.y + it.vel.y > height) it.vel.y *= -1
                it.pos += it.vel
                drawer.stroke = ColorRGBa.PINK
                if (it.pos.dist(mouse.position.x, mouse.position.y) < 200) {
                    drawer.lineSegment(mouse.position.x, mouse.position.y, it.pos.x, it.pos.y)
                }
                drawer.circle(it.pos.x, it.pos.y, size)
            }
            if (curAnim >= 0.0) {
                size = 10 + (size - 10) * easeInOutElastic(curAnim)
                curAnim -= deltaTime
            }
        }
    }
}

fun easeOutCirc(x: Double): Double {
    return sqrt(1 - (x - 1).pow(2.0))
}

fun easeInOutElastic(x: Double): Double {
    val c5 = (2 * PI) / 4.5
    return when {
        x == 0.0 -> 0.0
        x == 1.0 -> 1.0
        x < 0.5 -> -(2.0.pow(20.0 * x - 10) * sin((20.0 * x - 11.125) * c5) / 2.0)
        else -> (2.0.pow(-20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2.0 + 1
    }
}

fun initPoints(area: Vector2, num: Int): Array<Point> = Array(num) {
    Point(
        V2(Math.random() * area.x, Math.random() * area.y),
        V2((Math.random() - 0.5) * 10.0, (Math.random() - 0.5) * 10.0)
    )
}

operator fun V2.plusAssign(o: V2) {
    this.x += o.x
    this.y += o.y
}

class V2(var x: Double, var y: Double) {
    fun dist(o: V2): Double = sqrt((o.x - x) * (o.x - x) + (o.y - y) * (o.y - y))
    fun dist(x: Double, y: Double): Double = dist(V2(x, y))
}

class Point(val pos: V2, val vel: V2) {
}