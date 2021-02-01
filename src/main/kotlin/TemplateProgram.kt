import ddf.minim.Minim
import ddf.minim.analysis.FFT
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.math.abs

fun main() = application {
    configure {
        width = 1920
        height = 1080
    }

    program {
        val font = loadFont("data/fonts/default.otf", 24.0)
        val minim = Minim(object : Object() {
            fun sketchPath(fileName: String): String = fileName
            fun createInput(fileName: String): InputStream = FileInputStream(File(fileName))
        })
        minim.debugOn()
        val lineIn = minim.lineIn
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
            drawer.clear(ColorRGBa(bass / 75.0 + tre / 500.0, bass / 150.0, tre / 1000.0))
            drawer.fontMap = font
            drawer.fill = ColorRGBa.BLACK
            drawer.text("max: $max", 10.0, height / 2.0)
            drawer.text("fft: ${fft.getBand(0)}", 10.0, height / 2.0 + 20)
            drawer.text("bass: $bass", width - 300.0, 100.0)

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
