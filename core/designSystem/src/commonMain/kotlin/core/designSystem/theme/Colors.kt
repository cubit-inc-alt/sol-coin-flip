package core.designSystem.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val appColorScheme = lightColorScheme().copy(
  primary = AppColors.primary70,
  secondary = Color(0xFF64748B),
  secondaryContainer = Color(0xFFE2E8F0)
)

interface AppColors {
  val primary: Color
  val onPrimary: Color
  val primaryContainer: Color
  val onPrimaryContainer: Color
  val inversePrimary: Color
  val secondary: Color
  val onSecondary: Color
  val secondaryContainer: Color
  val onSecondaryContainer: Color
  val tertiary: Color
  val onTertiary: Color
  val tertiaryContainer: Color
  val onTertiaryContainer: Color
  val background: Color
  val onBackground: Color
  val surface: Color
  val onSurface: Color
  val surfaceVariant: Color
  val onSurfaceVariant: Color
  val surfaceTint: Color
  val inverseSurface: Color
  val inverseOnSurface: Color
  val error: Color
  val onError: Color
  val errorContainer: Color
  val onErrorContainer: Color
  val outline: Color
  val outlineVariant: Color
  val scrim: Color
  val surfaceBright: Color
  val surfaceDim: Color
  val surfaceContainer: Color
  val surfaceContainerHigh: Color
  val surfaceContainerHighest: Color
  val surfaceContainerLow: Color
  val surfaceContainerLowest: Color

  companion object Companion {
    val light: AppColors = CoinFlipLightColors
    val dark: AppColors = CoinFlipLightColors
    val black: AppColors = CoinFlipLightColors
    val primary10 = Color(0xFF2C2100)
    val primary20 = Color(0xFF4B3900)
    val primary30 = Color(0xFF6A5100)
    val primary40 = Color(0xFF896A00)
    val primary50 = Color(0xFFAA8300)
    val primary60 = Color(0xFFCC9D00)
    val primary70 = Color(0xFFEAB308)
    val primary80 = Color(0xFFFFCD41)
    val primary90 = Color(0xFFFFE088)
    val primary95 = Color(0xFFFFF0C2)
    val primary99 = Color(0xFFFFFBF1)
    val black10: Color = Color(0xFF3D3D3D)
    val black20: Color = Color(0xFF525252)
    val black30: Color = Color(0xFF666666)
    val black40: Color = Color(0xFF7A7A7A)
    val black50: Color = Color(0xFF8F8F8F)
    val black60: Color = Color(0xFFA3A3A3)
    val black70: Color = Color(0xFFB8B8B8)
    val black80: Color = Color(0xFFCCCCCC)
    val black90: Color = Color(0xFFE0E0E0)
    val black99: Color = Color(0xFFF5F5F5)
    val green10 = Color(0xFF084C17)
    val green20 = Color(0xFF126E26)
    val green30 = Color(0xFF1F9039)
    val green40 = Color(0xFF31B24E)
    val green50 = Color(0xFF46D466)
    val green60 = Color(0xFF60F681)
    val green70 = Color(0xFF86FFA1)
    val green80 = Color(0xFFA9FFBC)
    val green90 = Color(0xFFCCFFD8)
    val green99 = Color(0xFFEFFFF3)
    val yellow10 = Color(0xFF484100)
    val yellow20 = Color(0xFF6A6000)
    val yellow30 = Color(0xFF8C7E00)
    val yellow40 = Color(0xFFAE9D01)
    val yellow50 = Color(0xFFD0BD0D)
    val yellow60 = Color(0xFFF2DD1E)
    val yellow70 = Color(0xFFFFEE52)
    val yellow80 = Color(0xFFFFF384)
    val yellow90 = Color(0xFFFFF8B6)
    val yellow99 = Color(0xFFFFFDE8)
    val red10 = Color(0xFF540B0B)
    val red20 = Color(0xFF761616)
    val red30 = Color(0xFF982323)
    val red40 = Color(0xFFBA3434)
    val red50 = Color(0xFFDC4848)
    val red60 = Color(0xFFFE5F5F)
    val red70 = Color(0xFFFF8383)
    val red80 = Color(0xFFFFA7A7)
    val red90 = Color(0xFFFFCBCB)
    val red99 = Color(0xFFFFEFEF)
    val warning = Color(0xFFEF8D32)
    val active = Color(0xFF1DE189)
    val success = Color(0xFF22C55E)
    val infoBlackBg = Color(0xFF0F172A)
  }
}

private object CoinFlipLightColors : AppColors {
  override val primary: Color get() = appColorScheme.primary
  override val onPrimary: Color get() = appColorScheme.onPrimary
  override val primaryContainer: Color get() = appColorScheme.primaryContainer
  override val onPrimaryContainer: Color get() = appColorScheme.onPrimaryContainer
  override val inversePrimary: Color get() = appColorScheme.inversePrimary

  override val secondary: Color get() = appColorScheme.secondary
  override val onSecondary: Color get() = appColorScheme.onSecondary
  override val secondaryContainer: Color get() = appColorScheme.secondaryContainer
  override val onSecondaryContainer: Color get() = appColorScheme.onSecondaryContainer

  override val tertiary: Color get() = appColorScheme.tertiary
  override val onTertiary: Color get() = appColorScheme.onTertiary
  override val tertiaryContainer: Color get() = appColorScheme.tertiaryContainer
  override val onTertiaryContainer: Color get() = appColorScheme.onTertiaryContainer

  override val background: Color get() = appColorScheme.background
  override val onBackground: Color get() = appColorScheme.onBackground

  override val surface: Color get() = appColorScheme.surface
  override val onSurface: Color get() = appColorScheme.onSurface
  override val surfaceVariant: Color get() = appColorScheme.surfaceVariant
  override val onSurfaceVariant: Color get() = appColorScheme.onSurfaceVariant
  override val surfaceTint: Color get() = appColorScheme.surfaceTint

  override val inverseSurface: Color get() = appColorScheme.inverseSurface
  override val inverseOnSurface: Color get() = appColorScheme.inverseOnSurface

  override val error: Color get() = appColorScheme.error
  override val onError: Color get() = appColorScheme.onError
  override val errorContainer: Color get() = appColorScheme.errorContainer
  override val onErrorContainer: Color get() = appColorScheme.onErrorContainer

  override val outline: Color get() = appColorScheme.outline
  override val outlineVariant: Color get() = appColorScheme.outlineVariant

  override val scrim: Color get() = appColorScheme.scrim

  override val surfaceBright: Color get() = appColorScheme.surfaceBright
  override val surfaceDim: Color get() = appColorScheme.surfaceDim
  override val surfaceContainer: Color get() = appColorScheme.surfaceContainer
  override val surfaceContainerHigh: Color get() = appColorScheme.surfaceContainerHigh
  override val surfaceContainerHighest: Color get() = appColorScheme.surfaceContainerHighest
  override val surfaceContainerLow: Color get() = appColorScheme.surfaceContainerLow
  override val surfaceContainerLowest: Color get() = appColorScheme.surfaceContainerLowest
}
