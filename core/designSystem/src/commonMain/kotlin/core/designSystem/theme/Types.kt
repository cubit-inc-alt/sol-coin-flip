package core.designSystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import core.designSystem.generated.resources.BalooChettan_Regular
import core.designSystem.generated.resources.NunitoSans_Black
import core.designSystem.generated.resources.NunitoSans_Bold
import core.designSystem.generated.resources.NunitoSans_ExtraBold
import core.designSystem.generated.resources.NunitoSans_ExtraLight
import core.designSystem.generated.resources.NunitoSans_Light
import core.designSystem.generated.resources.NunitoSans_Regular
import core.designSystem.generated.resources.NunitoSans_SemiBold
import core.designSystem.generated.resources.Quicksand_Regular
import core.designSystem.generated.resources.Res
import org.jetbrains.compose.resources.Font
import core.designSystem.theme.PandaTextSize.size_12
import core.designSystem.theme.PandaTextSize.size_14
import core.designSystem.theme.PandaTextSize.size_16
import core.designSystem.theme.PandaTextSize.size_18
import core.designSystem.theme.PandaTextSize.size_20
import core.designSystem.theme.PandaTextSize.size_24
import core.designSystem.theme.PandaTextSize.size_32


val FontFamily.Companion.Nunito: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.NunitoSans_ExtraLight, FontWeight.ExtraLight),
        Font(Res.font.NunitoSans_Light, FontWeight.Light),
        Font(Res.font.NunitoSans_Regular, FontWeight.Normal),
        Font(Res.font.NunitoSans_Black, FontWeight.Black),
        Font(Res.font.NunitoSans_SemiBold, FontWeight.SemiBold),
        Font(Res.font.NunitoSans_Bold, FontWeight.Bold),
        Font(Res.font.NunitoSans_ExtraBold, FontWeight.ExtraBold),
    )

val FontFamily.Companion.Baloo: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.BalooChettan_Regular, FontWeight.Normal)
    )
val FontFamily.Companion.QuickSand: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.Quicksand_Regular, FontWeight.Normal)
    )

private val lineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

@Composable
private fun rememberTextStyle(
    fontSize: TextUnit,
    fontWeight: FontWeight,
    fontFamily: FontFamily = FontFamily.Nunito,
    letterSpacing: TextUnit = TextUnit.Unspecified,
): TextStyle {
    val colors = AppTheme.colors

    return remember(colors) {
        TextStyle(
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            lineHeightStyle = lineHeightStyle,
            color = colors.onBackground,
            letterSpacing = letterSpacing,
        )
    }
}

@Suppress("PropertyName")
interface AppTypography {
    val display: Display
    val heading: Heading
    val body: Body
    val label: Label

    interface Display {
        @get:Composable
        val SmallRegular: TextStyle

        @get:Composable
        val MediumRegular: TextStyle

        @get:Composable
        val LargeRegular: TextStyle

        @get:Composable
        val SmallSemiBold: TextStyle

        @get:Composable
        val MediumSemiBold: TextStyle

        @get:Composable
        val LargeSemiBold: TextStyle

        @get:Composable
        val SmallBold: TextStyle

        @get:Composable
        val MediumBold: TextStyle

        @get:Composable
        val LargeBold: TextStyle
    }


    interface Heading {
        @get:Composable
        val SmallRegular: TextStyle

        @get:Composable
        val MediumRegular: TextStyle

        @get:Composable
        val LargeRegular: TextStyle

        @get:Composable
        val SmallSemiBold: TextStyle

        @get:Composable
        val DefaultSemiBold: TextStyle

        @get:Composable
        val LargeSemiBold: TextStyle

        @get:Composable
        val SmallBold: TextStyle

        @get:Composable
        val DefaultBold: TextStyle

        @get:Composable
        val LargeBold: TextStyle
    }


    interface Body {
        @get:Composable
        val SmallRegular: TextStyle

        @get:Composable
        val MediumRegular: TextStyle

        @get:Composable
        val LargeRegular: TextStyle

        @get:Composable
        val SmallNormal: TextStyle

        @get:Composable
        val MediumNormal: TextStyle

        @get:Composable
        val LargeNormal: TextStyle

        @get:Composable
        val SmallSemiBold: TextStyle

        @get:Composable
        val DefaultSemiBold: TextStyle

        @get:Composable
        val LargeSemiBold: TextStyle

        @get:Composable
        val SmallBold: TextStyle

        @get:Composable
        val DefaultBold: TextStyle

        @get:Composable
        val LargeBold: TextStyle
    }

    interface Label {
        @get:Composable
        val SmallRegular: TextStyle

        @get:Composable
        val MediumRegular: TextStyle

        @get:Composable
        val LargeRegular: TextStyle

        @get:Composable
        val SmallNormal: TextStyle

        @get:Composable
        val MediumNormal: TextStyle

        @get:Composable
        val LargeNormal: TextStyle

        @get:Composable
        val SmallSemiBold: TextStyle

        @get:Composable
        val DefaultSemiBold: TextStyle

        @get:Composable
        val LargeSemiBold: TextStyle

        @get:Composable
        val SmallBold: TextStyle

        @get:Composable
        val DefaultBold: TextStyle

        @get:Composable
        val LargeBold: TextStyle
    }

}

internal object PandaFontTypography : AppTypography {
    override val display = object : AppTypography.Display {
        override val SmallRegular @Composable get() = rememberTextStyle(size_18, FontWeight.Normal)
        override val MediumRegular @Composable get() = rememberTextStyle(size_24, FontWeight.Normal)
        override val LargeRegular @Composable get() = rememberTextStyle(size_32, FontWeight.Normal)

        override val SmallSemiBold
            @Composable get() = rememberTextStyle(
                size_18,
                FontWeight.SemiBold
            )
        override val MediumSemiBold
            @Composable get() = rememberTextStyle(
                size_24,
                FontWeight.SemiBold
            )
        override val LargeSemiBold
            @Composable get() = rememberTextStyle(
                size_32,
                FontWeight.SemiBold
            )

        override val SmallBold @Composable get() = rememberTextStyle(size_18, FontWeight.Bold)
        override val MediumBold @Composable get() = rememberTextStyle(size_24, FontWeight.Bold)
        override val LargeBold @Composable get() = rememberTextStyle(size_32, FontWeight.Bold)
    }


    override val heading = object : AppTypography.Heading {
        override val SmallRegular @Composable get() = rememberTextStyle(size_14, FontWeight.Normal)
        override val MediumRegular @Composable get() = rememberTextStyle(size_16, FontWeight.Normal)
        override val LargeRegular @Composable get() = rememberTextStyle(size_20, FontWeight.Normal)

        override val SmallSemiBold
            @Composable get() = rememberTextStyle(
                size_14,
                FontWeight.SemiBold
            )
        override val DefaultSemiBold
            @Composable get() = rememberTextStyle(
                size_16,
                FontWeight.SemiBold
            )
        override val LargeSemiBold
            @Composable get() = rememberTextStyle(
                size_20,
                FontWeight.SemiBold
            )

        override val SmallBold @Composable get() = rememberTextStyle(size_14, FontWeight.Bold)
        override val DefaultBold @Composable get() = rememberTextStyle(size_16, FontWeight.Bold)
        override val LargeBold @Composable get() = rememberTextStyle(size_20, FontWeight.Bold)
    }

    override val body: AppTypography.Body = object : AppTypography.Body {
        override val SmallRegular @Composable get() = rememberTextStyle(size_12, FontWeight.Normal)
        override val MediumRegular @Composable get() = rememberTextStyle(size_14, FontWeight.Normal)
        override val LargeRegular @Composable get() = rememberTextStyle(size_16, FontWeight.Normal)

        override val SmallNormal @Composable get() = SmallRegular
        override val MediumNormal @Composable get() = MediumRegular
        override val LargeNormal @Composable get() = LargeRegular

        override val SmallSemiBold
            @Composable get() = rememberTextStyle(
                size_12,
                FontWeight.SemiBold
            )
        override val DefaultSemiBold
            @Composable get() = rememberTextStyle(
                size_14,
                FontWeight.SemiBold
            )
        override val LargeSemiBold
            @Composable get() = rememberTextStyle(
                size_16,
                FontWeight.SemiBold
            )

        override val SmallBold @Composable get() = rememberTextStyle(size_12, FontWeight.Bold)
        override val DefaultBold @Composable get() = rememberTextStyle(size_14, FontWeight.Bold)
        override val LargeBold @Composable get() = rememberTextStyle(size_16, FontWeight.Bold)
    }
    override val label: AppTypography.Label = object : AppTypography.Label {
        override val SmallRegular @Composable get() = rememberTextStyle(size_12, FontWeight.Normal)
        override val MediumRegular @Composable get() = rememberTextStyle(size_14, FontWeight.Normal)
        override val LargeRegular @Composable get() = rememberTextStyle(size_16, FontWeight.Normal)

        override val SmallNormal @Composable get() = SmallRegular
        override val MediumNormal @Composable get() = MediumRegular
        override val LargeNormal @Composable get() = LargeRegular

        override val SmallSemiBold
            @Composable get() = rememberTextStyle(
                size_12,
                FontWeight.SemiBold
            )
        override val DefaultSemiBold
            @Composable get() = rememberTextStyle(
                size_14,
                FontWeight.SemiBold
            )
        override val LargeSemiBold
            @Composable get() = rememberTextStyle(
                size_16,
                FontWeight.SemiBold
            )

        override val SmallBold @Composable get() = rememberTextStyle(size_12, FontWeight.Bold)
        override val DefaultBold @Composable get() = rememberTextStyle(size_14, FontWeight.Bold)
        override val LargeBold @Composable get() = rememberTextStyle(size_16, FontWeight.Bold)
    }

}
