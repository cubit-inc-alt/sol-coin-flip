package core.resources

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

val StringResource.res
  @Composable get() = stringResource(this)


@Composable
fun stringResourceOrNull(id: StringResource?): String? {
  return id?.let { stringResource(it) }
}

val String.getMessage: String
  @Composable
  get() = RESPONSE_ERROR_MESSAGE_BY_STATUS[this]?.res ?: this


