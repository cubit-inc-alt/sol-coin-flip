package core.designSystem.theme

enum class UserThemeMode {
  System,
  Light,
  Dark,
  Black,
}

enum class ThemeMode {
  Light,
  Dark,
  Black,
  ;

  fun isDark(): Boolean = this != Light
}
