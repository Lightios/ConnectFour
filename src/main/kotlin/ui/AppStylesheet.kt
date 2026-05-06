package ui

import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*


object AppStyleSheet : StyleSheet() {

    // ── Design tokens ────────────────────────────────────────────────
    object Colors {
        val background = Color("#1a1a2e")
        val surface = Color("#16213e")
        val surfaceRaised = Color("#0f3460")
        val border = Color("#e94560")

        val red = Color("#e94560")
        val redGlow = Color("rgba(233, 69, 96, 0.4)")
        val yellow = Color("#f5a623")
        val yellowGlow = Color("rgba(245, 166, 35, 0.4)")

        val cellEmpty = Color("#0a1628")
        val cellBorder = Color("#1e3a5f")

        val textPrimary = Color("#eaeaea")
        val textMuted = Color("#7a8ba0")
        val textAccent = Color("#e94560")

        val winHighlight = Color("rgba(255, 255, 255, 0.15)")
        val buttonPrimary = Color("#e94560")
        val buttonHover = Color("#c73652")
        val overlay = Color("rgba(0, 0, 0, 0.6)")
    }

    object Tokens {
        val radiusSm = 6.px
        val radiusMd = 12.px
        val radiusLg = 20.px
        val radiusFull = 50.percent

        val shadowCard = "0 8px 32px rgba(0,0,0,0.4)"
        val shadowGlow = { player: String ->
            if (player == "red")
                "0 0 16px rgba(233,69,96,0.5)"
            else
                "0 0 16px rgba(245,166,35,0.5)"
        }
//        val shadowGlow = (player: String) -> if (player == "red")
//        "0 0 16px rgba(233,69,96,0.5)" else "0 0 16px rgba(245,166,35,0.5)"
    }

    // ── Global reset ─────────────────────────────────────────────────
    init {
        "*, *::before, *::after" style {
            property("box-sizing", "border-box")
            property("margin", "0")
            property("padding", "0")
        }

        "body" style {
            backgroundColor(Colors.background)
            color(Colors.textPrimary)
            fontFamily("'Segoe UI'", "system-ui", "sans-serif")
            property("min-height", "100vh")
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
        }

        "#root" style {
            width(100.percent)
            property("min-height", "100vh")
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
        }
    }

    // ── Layout ───────────────────────────────────────────────────────
    val screen by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        gap(24.px)
        padding(24.px)
        width(100.percent)
        property("max-width", "900px")
    }

    val card by style {
        backgroundColor(Colors.surface)
        borderRadius(Tokens.radiusLg)
        padding(32.px)
        property("box-shadow", Tokens.shadowCard)
        width(100.percent)
    }

    // ── Typography ───────────────────────────────────────────────────
    val title by style {
        fontSize(2.cssRem)
        fontWeight(700)
        color(Colors.textPrimary)
        textAlign("center")
        property("letter-spacing", "0.05em")
        property("text-transform", "uppercase")
    }

    val subtitle by style {
        fontSize(1.cssRem)
        color(Colors.textMuted)
        textAlign("center")
    }

    val label by style {
        fontSize(0.875.cssRem)
        color(Colors.textMuted)
        property("text-transform", "uppercase")
        property("letter-spacing", "0.08em")
        fontWeight(600)
    }

    // ── Form elements ────────────────────────────────────────────────
    val configForm by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        gap(20.px)
        width(100.percent)
        property("max-width", "360px")
    }

    val inputGroup by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        gap(8.px)
    }

    val input by style {
        backgroundColor(Colors.cellEmpty)
        color(Colors.textPrimary)
        border(1.px, LineStyle.Solid, Colors.cellBorder)
        borderRadius(Tokens.radiusSm)
        padding(10.px, 14.px)
        fontSize(1.cssRem)
        width(100.percent)
        property("outline", "none")
        property("transition", "border-color 0.2s")

        self + focus style {
            border { color(Colors.border) }
        }
    }

    // ── Buttons ──────────────────────────────────────────────────────
    val button by style {
        backgroundColor(Colors.buttonPrimary)
        color(Colors.textPrimary)
        border(0.px, LineStyle.None, Color.transparent)
        borderRadius(Tokens.radiusSm)
        padding(12.px, 24.px)
        fontSize(1.cssRem)
        fontWeight(600)
        property("cursor", "pointer")
        property("transition", "background-color 0.2s, transform 0.1s")
        property("text-transform", "uppercase")
        property("letter-spacing", "0.05em")

        self + hover style {
            backgroundColor(Colors.buttonHover)
            property("transform", "translateY(-1px)")
        }

        self + active style {
            property("transform", "translateY(0)")
        }
    }

    val buttonOutline by style {
        backgroundColor(Color.transparent)
        color(Colors.textMuted)
        border(1.px, LineStyle.Solid, Colors.cellBorder)
        borderRadius(Tokens.radiusSm)
        padding(8.px, 16.px)
        fontSize(0.875.cssRem)
        property("cursor", "pointer")
        property("transition", "border-color 0.2s, color 0.2s")

        self + hover style {
            border(
                color = Colors.textMuted
            )
            color(Colors.textPrimary)
        }
    }

    // ── Status bar ───────────────────────────────────────────────────
    val statusBar by style {
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.SpaceBetween)
        alignItems(AlignItems.Center)
        width(100.percent)
        gap(12.px)
    }

    val playerIndicator by style {
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        gap(10.px)
        fontSize(1.cssRem)
        fontWeight(600)
    }

    val playerDot by style {
        width(16.px)
        height(16.px)
        borderRadius(Tokens.radiusFull)
        property("flex-shrink", "0")
    }

    val statusMessage by style {
        fontSize(1.1.cssRem)
        fontWeight(700)
        textAlign("center")
        property("flex", "1")
    }

    // ── Board ────────────────────────────────────────────────────────
    val boardWrapper by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignItems(AlignItems.Center)
        gap(8.px)
        width(100.percent)
    }

    val columnHints by style {
        display(DisplayStyle.Flex)
        width(100.percent)
        gap(4.px)
    }

    val columnHint by style {
        property("flex", "1")
        height(24.px)
        borderRadius(Tokens.radiusSm)
        property("cursor", "pointer")
        property("transition", "background-color 0.15s")
        backgroundColor(Color.transparent)
        border(0.px, LineStyle.None, Color.transparent)

        self + hover style {
            backgroundColor(Colors.winHighlight)
        }
    }

    val board by style {
        display(DisplayStyle.Grid)
        gap(4.px)
        backgroundColor(Colors.surfaceRaised)
        borderRadius(Tokens.radiusMd)
        padding(12.px)
        property("box-shadow", Tokens.shadowCard)
        width(100.percent)
        justifyContent(JustifyContent.SpaceBetween)
    }

    // ── Cells ────────────────────────────────────────────────────────
    val cell by style {
        borderRadius(Tokens.radiusFull)
        property("aspect-ratio", "1")
        width(100.percent)
        property("transition", "box-shadow 0.2s")

    }

    val cellEmpty by style {
        backgroundColor(Colors.cellEmpty)
        border(1.px, LineStyle.Solid, Colors.cellBorder)
    }

    val cellRed by style {
        backgroundColor(Colors.red)
        property("box-shadow", "0 0 8px rgba(233,69,96,0.3)")
    }

    val cellYellow by style {
        backgroundColor(Colors.yellow)
        property("box-shadow", "0 0 8px rgba(245,166,35,0.3)")
    }

    val cellWinning by style {
        property("box-shadow", "0 0 20px rgba(255,255,255,0.6), 0 0 40px rgba(255,255,255,0.3)")
        property("animation", "pulse 0.8s ease-in-out infinite alternate")
    }

    @OptIn(ExperimentalComposeWebApi::class)
// Changed '=' to 'by' so the stylesheet can implicitly name the keyframe "fadeIn"
    private val fadeIn by keyframes {
        from {
            transform { translateY((-400).percent)}
            opacity(0.6)
        }
        to {
            transform { translateY(0.percent)}
            opacity(1)
        }
    }

    // ── Animations ───────────────────────────────────────────────────
    val dropAnimation by style {
        animation(keyframes = fadeIn) {
            duration(0.3.s)
            timingFunction(AnimationTimingFunction.EaseIn)
        }
    }


    // ── Responsive ───────────────────────────────────────────────────
    init {
        media("(max-width: 600px)") {
            style {
                padding(12.px)
                gap(12.px)
            }

            ".${card}" style {
                padding(16.px)
                borderRadius(Tokens.radiusMd)
            }

            ".${title}" style {
                fontSize(1.5.cssRem)
            }
        }
    }

}