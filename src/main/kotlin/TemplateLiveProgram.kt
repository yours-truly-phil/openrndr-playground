import ddf.minim.Minim
import ddf.minim.analysis.FFT
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.extra.olive.oliveProgram
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

fun main() = application {
    configure {
        width = 1920
        height = 1080
    }
    oliveProgram {
        val font = loadFont("data/fonts/default.otf", 24.0)
        val minim = Minim(object : Object() {
            fun sketchPath(fileName: String): String = fileName
            fun createInput(fileName: String): InputStream = FileInputStream(File(fileName))
        })
        minim.debugOn()
        val lineIn = minim.lineIn

        val fft = FFT(lineIn.bufferSize(), lineIn.sampleRate())
        extend {
            drawer.clear(ColorRGBa.PINK)
            drawer.fontMap = font
            drawer.fill = ColorRGBa.BLACK
            for (i in 0 until lineIn.bufferSize()) {
                drawer.rectangle(i.toDouble() * 10 + 2*i, height / 2.0, 10.0, 10+lineIn.mix[i].toDouble())
            }
        }
    }
}