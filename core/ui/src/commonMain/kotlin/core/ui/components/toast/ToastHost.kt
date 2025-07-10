package core.ui.components.toast

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.designSystem.theme.AppColors.Companion.warning
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.dimens
import core.ui.components.toast.components.ToastView


val LocalToastManager = compositionLocalOf<ToastManager> {
    error("No ToastManager provided")
}

@Composable
internal fun rememberToastManager(): ToastManager {
    return remember { ToastManager() }
}

private val ToastTransitionSpec: AnimatedContentTransitionScope<Toast?>.() -> ContentTransform = {
    val transition = if (targetState == null) {
        EnterTransition.None.togetherWith(slideOutOfContainer(SlideDirection.Down).plus(fadeOut()))
    } else {
        slideIntoContainer(SlideDirection.Up, tween(delayMillis = 100))
            .plus(fadeIn())
            .togetherWith(fadeOut())
    }
    transition.using(SizeTransform(false))
}

@Composable
fun BoxScope.ToastHost(content: @Composable () -> Unit) {
    val toastManager = rememberToastManager()

    CompositionLocalProvider(LocalToastManager provides toastManager) {
        content()
    }
    ToastContent(toastManager)
}

@Composable
private fun BoxScope.ToastContent(toastManager: ToastManager) {
    val toasts by toastManager.toasts.collectAsState()
    val toast by remember { derivedStateOf { toasts.firstOrNull() } }

    AnimatedContent(
        targetState = toast,
        modifier = Modifier
            .align(Alignment.BottomCenter),
        contentAlignment = Alignment.Center,
        transitionSpec = ToastTransitionSpec,
    ) { it ->
        it?.also {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = dimens.size_14, start = dimens.size_4, end = dimens.size_4),
                contentAlignment = Alignment.Center,
            ) {
                ToastView(it, toastManager::removeToast)
            }
        }
    }
}

@Stable
internal val Toast.typeColor
    @Stable
    @Composable
    @ReadOnlyComposable
    get() = when (this.type) {
        Toast.Type.Message -> colors.onBackground
        Toast.Type.Alert -> warning
        Toast.Type.Error -> colors.error
    }
