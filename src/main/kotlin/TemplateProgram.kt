import ddf.minim.Minim
import ddf.minim.analysis.FFT
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.math.*

fun main() = application {
    configure {
        width = 1280
        height = 768
    }

    program {
        val font = loadFont("data/fonts/default.otf", 24.0)
        val minim = Minim(object : Object() {
            fun sketchPath(fileName: String): String = fileName
            fun createInput(fileName: String): InputStream = FileInputStream(File(fileName))
        })
        minim.debugOn()
        val lineIn = minim.getLineIn(Minim.MONO)
        val fft = FFT(lineIn.bufferSize(), lineIn.sampleRate())

        extend {
            val buf = lineIn.bufferSize()
            fft.forward(lineIn.left)
            var max = 0.0
            for (i in 0 until buf) max = maxOf(max, lineIn.left[i].toDouble())
            var bass = 0.0
            for (i in 0 until 4) bass += fft.getBand(i)
            var tre = 0.0
            for (i in 20 until 60) tre += fft.getBand(i)
            drawer.clear(
                ColorRGBa(
                    (bass / 25.0 + tre / 125.0) * (sin(seconds) + 1) / 2.0,
                    (bass / 50.0) * (sin(seconds + PI) + 1) / 2.0,
                    (tre / 250.0) * (cos(seconds) + 1) / 2.0
                )
            )

            drawer.fontMap = font
            drawer.fill = ColorRGBa.BLACK

            drawer.fill = ColorRGBa.WHITE
            drawer.stroke = ColorRGBa.BLUE
            var off = 0.0
            for (i in 0 until fft.specSize()) {
                val barWidth = (fft.specSize() / (i.toDouble() + 1)) / 3.0
                drawer.rectangle(off, height.toDouble(), barWidth, -fft.getBand(i) * 50.0)
                off += barWidth
            }

            drawer.translate(width / 2.0, height / 2.0)
            drawer.rotate(10.0)
            drawer.stroke = null

            for (i in 0 until buf) {
                val sample = lineIn.left[i]
                val d = buf.toDouble()
                val v = abs(sample.toDouble())
                drawer.fill = ColorRGBa(v, i / d, i / d)
                drawer.rotate(340.0 / lineIn.bufferSize())
                drawer.rectangle(
                    0.0, 50.0,
                    3.0, 1000 * v
                )
            }
        }
    }
}
