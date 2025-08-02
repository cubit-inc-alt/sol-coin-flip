package core.features.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.designSystem.elements.DefaultTonalButton
import core.designSystem.theme.AppColors
import core.designSystem.theme.AppColors.Companion.infoBlackBg
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_28
import core.designSystem.theme.AppDimensions.size_30
import core.designSystem.theme.AppDimensions.size_4
import core.designSystem.theme.AppDimensions.size_40
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.shapes
import core.designSystem.theme.AppTheme.typography
import core.features.main.cards.PlayInfoCard
import core.features.main.customview.ConfirmWagerBottomSheet
import core.models.DoubleOrNothing
import core.models.SolNetwork
import core.models.tnxAsExplorerLink
import core.resources.generated.resources.Res
import core.resources.generated.resources.background_blurred_light
import core.resources.generated.resources.btn_tap_to_flip
import core.resources.generated.resources.flip_warning
import core.resources.generated.resources.ic_active
import core.resources.generated.resources.ic_info
import core.resources.generated.resources.ic_wallet
import core.resources.generated.resources.img_coin
import core.resources.generated.resources.real_sol_transaction
import core.resources.generated.resources.recent_plays
import core.resources.generated.resources.wager
import core.ui.components.CustomSlider
import core.ui.rememberMutableStateOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
  viewModel: MainScreenViewModel,
) {
  val state by viewModel.state.collectAsState()
  val playList by viewModel.playList.collectAsState()

  var showPlayEndAlert by rememberMutableStateOf(false)

  val flipStatus by viewModel.flipStatus.collectAsStateWithLifecycle()

  Scaffold { padding ->
    Box(Modifier.fillMaxSize()) {
      Image(
        painter = painterResource(Res.drawable.background_blurred_light),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(size_16),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(bottom = size_4),
          horizontalArrangement = Arrangement.spacedBy(size_16),
          verticalAlignment = Alignment.CenterVertically
        ) {

          DefaultTonalButton(
            Modifier.wrapContentSize(),
            text = "Devnet",
            startIcon = Res.drawable.ic_wallet,
            padding = PaddingValues(horizontal = size_16, vertical = 0.dp),
            endIcon = Res.drawable.ic_active,
            endIconTint = if (state.isWalletConnected) AppColors.active else Color.Red,
          ) {
            if (!state.isWalletConnected) viewModel.connect()
          }

          Spacer(modifier = Modifier.weight(1f))

          Text(
            text = state.accountText,
            style = typography.body.DefaultSemiBold,
            color = colors.secondary,
            modifier = Modifier.wrapContentWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
          )
        }

        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          item {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth().padding(top = size_28),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  stringResource(Res.string.real_sol_transaction),
                  style = typography.body.LargeSemiBold,
                  color = colors.secondary
                )
                Image(
                  painter = painterResource(Res.drawable.ic_info),
                  contentDescription = "",
                  modifier = Modifier.wrapContentHeight().padding(start = size_8),
                  colorFilter = if (showPlayEndAlert == null) ColorFilter.tint(Color.Black) else null
                )
              }

              Box(Modifier.fillMaxWidth().wrapContentHeight()) {

                Image(
                  painter = painterResource(Res.drawable.img_coin),
                  contentDescription = "",
                  modifier = Modifier.size(260.dp).padding(vertical = size_40).align(Alignment.Center)
                )

                Image(
                  painter = painterResource(Res.drawable.btn_tap_to_flip),
                  contentScale = ContentScale.FillWidth,
                  modifier = Modifier.align(Alignment.BottomCenter).width(220.dp).wrapContentHeight()
                    .padding(horizontal = size_24).clickable {
                      viewModel.flipCoin()
                    },
                  contentDescription = ""
                )

                if (showPlayEndAlert == null) {
                  Text(
                    stringResource(Res.string.flip_warning),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(top = size_12).clip(shapes.large)
                      .background(infoBlackBg)
                      .padding(size_12)
                  )
                }
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = size_30),
                horizontalArrangement = Arrangement.Center
              ) {

                Text(text = stringResource(Res.string.wager), style = typography.heading.DefaultSemiBold)

                Text(
                  text = state.formattedWager,
                  style = typography.heading.DefaultBold,
                  color = colors.primary,
                  modifier = Modifier.padding(start = 2.dp)
                )
              }

              CustomSlider(state.wager.toFloat() / 100f) {
                viewModel.setWager(it)
              }

              Text(
                stringResource(Res.string.recent_plays),
                color = colors.secondary,
                style = typography.body.DefaultSemiBold,
                modifier = Modifier.fillMaxWidth().padding(top = size_12),
                textAlign = TextAlign.Start
              )
            }
          }

          items(playList) {
            PlayInfoCard(
              modifier = Modifier.fillMaxWidth().padding(top = size_12),
              play = it
            )
          }

          if (playList.isEmpty()) {
            item("placeholder") {
              RecentPlayPlaceholder()
            }
          }
        }
      }
    }
  }

  (flipStatus as? FlipStatus.WaitingUserConfirmation)?.also { status ->
    ConfirmWagerBottomSheet(
      amount = status.formattedWager,
      token = "SOL",
      outcomes = status.formattedPossibleOutcome,
      onDismissRequest = { viewModel.clearFlipStatus() },
      onConfirmWager = {
        viewModel.confirmTransaction(status)
      })
  }

  flipStatus?.also {
    if (flipStatus !is FlipStatus.WaitingUserConfirmation) {
      FlipStatusDialog(flipStatus!!) {
        viewModel.clearFlipStatus()
      }
    }
  }
}

@Composable
fun RecentPlayPlaceholder() {
  Box(
    modifier = Modifier.fillMaxWidth()
      .padding(size_40),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "Fetching recent plays",
      color = colors.secondary,
      style = typography.body.DefaultSemiBold,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
fun FlipStatusDialog(
  flipStatus: FlipStatus,
  onDismiss: () -> Unit
) {

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false
    ),
  ) {

    val title =
      when (flipStatus) {
        is FlipStatus.FlipSuccess -> if (flipStatus.result == DoubleOrNothing.Double) "Head" else "Tails"
        else -> flipStatus.formattedWager
      }

    val tnxHash = remember(flipStatus) {
      (flipStatus as? FlipStatus.WaitingForChainConformation)?.tnxHash
        ?: (flipStatus as? FlipStatus.FlipSuccess)?.tnxHash
    }

    val uriHandler = LocalUriHandler.current

    Card(
      modifier = Modifier
        .padding(size_24)
        .animateContentSize()
        .fillMaxWidth(),
    ) {

      Column(
        modifier = Modifier.padding(size_16).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(size_8),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {

        Text(
          text = title,
          style = typography.heading.LargeBold,
          color = colors.primary,
          modifier = Modifier
            .align(Alignment.CenterHorizontally)
        )

        Box(
          modifier = Modifier
            .padding(top = size_24, bottom = size_16)
            .fillMaxWidth(),
        ) {
          when (flipStatus) {
            is FlipStatus.WaitingForChainConformation,
            is FlipStatus.SendingTransaction -> {

              val infiniteTransition = rememberInfiniteTransition("rotating")

              val animatedRotation by infiniteTransition.animateFloat(
                0f, 360f, animationSpec = infiniteRepeatable(
                  animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                  repeatMode = RepeatMode.Restart
                )
              )

              Column(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Image(
                  painter = painterResource(Res.drawable.img_coin),
                  contentDescription = "Coin rotating",
                  modifier = Modifier
                    .height(100.dp)
                    .graphicsLayer {
                      rotationY = animatedRotation
                    }
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
                )

                Row(
                  modifier = Modifier
                    .padding(top = size_16)
                    .align(Alignment.CenterHorizontally)
                ) {

                  Text(
                    text = flipStatus.formattedOutcomePositive,
                    style = typography.body.DefaultSemiBold,
                    color = AppColors.active,
                    modifier = Modifier
                  )

                  Text(
                    text = " OR ",
                    style = typography.body.DefaultSemiBold,
                    modifier = Modifier
                  )

                  Text(
                    text = flipStatus.formattedOutcomeNegative,
                    style = typography.body.DefaultSemiBold,
                    color = Color.Red,
                    modifier = Modifier
                  )
                }
              }
            }

            is FlipStatus.FlipSuccess -> {

              Column(
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Text(
                  text = flipStatus.formattedOutcome,
                  style = typography.heading.LargeBold,
                  color = if (flipStatus.result == DoubleOrNothing.Double) Color.Green else Color.Red,
                  textAlign = TextAlign.Center,
                  modifier = Modifier
                    .fillMaxWidth()
                )
              }


            }

            is FlipStatus.WaitingUserConfirmation -> {}
          }
        }

        tnxHash?.also {
          Text(
            text = "View on Chain",
            style = typography.body.DefaultMedium,
            color = colors.secondary,
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
          )

          Text(
            text = it,
            style = typography.body.DefaultMedium,
            color = colors.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
              .offset(y = (-4).dp)
              .clickable {
                uriHandler.openUri(it.tnxAsExplorerLink(SolNetwork.DevNet))
              }
              .padding(bottom = size_4)
          )
        }
      }
    }
  }
}
