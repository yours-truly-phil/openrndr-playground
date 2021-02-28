import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.panel.ControlManager
import org.openrndr.panel.elements.button
import org.openrndr.panel.elements.clicked
import org.openrndr.panel.layout
import org.openrndr.shape.Rectangle
import kotlin.math.*

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

fun main() = application {
    configure {
        width = 1280
        height = 768
    }
    oliveProgram {
        var tweenFun = { x: Double -> x }
        var anim = 0.0
        val easeInSine = { x: Double -> 1 - cos((x * PI) / 2.0) }
        val easeInCubic = { x: Double -> x * x * x }
        val easeInQuint = { x: Double -> x * x * x * x * x }
        val easeInCirc = { x: Double -> 1 - sqrt(1 - x.pow(2)) }
        val easeInOutBack = { x: Double ->
            val c1 = 1.70158
            val c2 = c1 * 1.525
            when {
                x < 0.5 -> ((2.0 * x).pow(2.0) * ((c2 + 1) * 2 * x - c2)) / 2.0
                else -> ((2.0 * x - 2.0).pow(2.0) * ((c2 + 1) * (x * 2.0 - 2.0) + c2) + 2.0) / 2.0
            }
        }
        val easeInElastic = { x: Double ->
            val c4 = (2 * PI) / 2.0
            -1.0 * 2.0.pow(10 * x - 10) * sin((x * 10.0 - 10.75) * c4)
        }
        val easeInOutExpo = { x: Double ->
            when {
                x == 0.0 -> 0.0
                x == 1.0 -> 1.0
                x < 0.5 -> 2.0.pow(20 * x - 10) / 2.0
                else -> (2 - 2.0.pow(-20 * x + 10)) / 2.0
            }
        }
        val easeOutBounce = { x: Double ->
            val n1 = 7.5625
            val d1 = 2.75
            when {
                x < 1 / d1 -> n1 * x * x
                x < 2 / d1 -> {
                    val x2 = x - 1.5 / d1
                    n1 * x2 * x2 + 0.75
                }
                x < 2.5 / d1 -> {
                    val x2 = x - 2.25 / d1
                    n1 * x2 * x2 + 0.9375
                }
                else -> {
                    val x2 = x - 2.625 / d1
                    n1 * x2 * x2 + 0.984375
                }
            }
        }
        val easeInOutBounce = { x: Double ->
            when {
                x < 0.5 -> (1.0 - easeOutBounce(1.0 - 2.0 * x)) / 2.0
                else -> (1.0 + easeOutBounce(2.0 * x - 1.0)) / 2.0
            }
        }
        extend(ControlManager()) {
            layout {
                button {
                    label = "easeInSine"
                    clicked {
                        tweenFun = easeInSine
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInCubic"
                    clicked {
                        tweenFun = easeInCubic
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInQuint"
                    clicked {
                        tweenFun = easeInQuint
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInCirc"
                    clicked {
                        tweenFun = easeInCirc
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInElastic"
                    clicked {
                        tweenFun = easeInElastic
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInOutExpo"
                    clicked {
                        tweenFun = easeInOutExpo
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInOutBack"
                    clicked {
                        tweenFun = easeInOutBack
                        anim = 0.0
                    }
                }
                button {
                    label = "easeOutBounce"
                    clicked {
                        tweenFun = easeOutBounce
                        anim = 0.0
                    }
                }
                button {
                    label = "easeInOutBounce"
                    clicked {
                        tweenFun = easeInOutBounce
                        anim = 0.0
                    }
                }
            }
        }
        extend {
            drawer.clear(ColorRGBa.GRAY.shade(0.250))
            val rect = Rectangle(300.0, 300.0, width - 400.0, height - 500.0)
            drawer.stroke = ColorRGBa.PINK
            drawer.fill = null
            drawer.rectangle(rect)
            (0..rect.width.toInt()).forEach { x ->
                drawer.lineSegment(
                    rect.x + x - 1.0, rect.y + tweenFun.invoke((x - 1.0) / rect.width) * rect.height,
                    rect.x + x.toDouble(), rect.y + tweenFun.invoke(x.toDouble() / rect.width) * rect.height
                )
            }
            drawer.fill = ColorRGBa.PINK
            drawer.stroke = null
            if (anim <= 1.0) {
                drawer.circle(anim * rect.width + rect.x, tweenFun.invoke(anim) * rect.height + rect.y, 10.0)
                anim += deltaTime * 0.8
            }
        }
    }
}