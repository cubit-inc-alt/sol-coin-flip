package core.common

enum class Platform {
    Android,
    IOS,
}

expect fun platform(): Platform

expect fun isDebugBuild(): Boolean
