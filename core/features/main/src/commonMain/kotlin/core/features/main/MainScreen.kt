package core.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key.Companion.P
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.designSystem.elements.DefaultTonalButton
import core.designSystem.theme.AppColors
import core.designSystem.theme.AppColors.Companion.infoBlackBg
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_26
import core.designSystem.theme.AppDimensions.size_28
import core.designSystem.theme.AppDimensions.size_30
import core.designSystem.theme.AppDimensions.size_40
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.shapes
import core.designSystem.theme.AppTheme.typography
import core.features.main.cards.PlayInfoCard
import core.features.main.customview.ConfirmWagerBottomSheet
import core.features.main.customview.PlayEndAlert
import core.resources.generated.resources.Res
import core.resources.generated.resources.background_blurred_light
import core.resources.generated.resources.btn_tap_to_flip
import core.resources.generated.resources.flip_warning
import core.resources.generated.resources.ic_active
import core.resources.generated.resources.ic_info
import core.resources.generated.resources.ic_wallet
import core.resources.generated.resources.img_coin
import core.resources.generated.resources.jup
import core.resources.generated.resources.real_sol_transaction
import core.resources.generated.resources.recent_plays
import core.resources.generated.resources.skr
import core.resources.generated.resources.sol
import core.resources.generated.resources.wager
import core.ui.components.CustomSlider
import core.ui.navigation.AppNavigation
import core.ui.rememberDerivedStateOf
import core.ui.rememberMutableStateOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


fun Float.formatAsCurrency(): String {

  val fraction = ((this - toInt()) * 100)
    .toInt()
    .toString()
    .padStart(2, '0')

  return "${toInt().toString().padStart(2, '0')}.$fraction"
}


@Composable
fun MainScreen(
  viewModel: MainScreenViewModel,
  navigateToNext: (AppNavigation) -> Unit
) {
  val state by viewModel.state.collectAsState()
  val playList by viewModel.playList.collectAsState()
  var showConfirmWager by rememberMutableStateOf<Boolean?>(false)
  var showPlayEndAlert by rememberMutableStateOf<Boolean?>(false)

  val formattedWager by rememberDerivedStateOf {
    state.wager.formatAsCurrency().let { "$it SOL" }
  }

  val formattedOutcome by rememberDerivedStateOf {
    "+${state.wager.times(2).formatAsCurrency()} SOL / -${state.wager.formatAsCurrency()} SOL"
  }

  if (showConfirmWager == true) {
    ConfirmWagerBottomSheet(
      amount = formattedWager,
      token = "SOL",
      outcomes = formattedOutcome,
      onDismissRequest = {
        showConfirmWager = it
      },
      onConfirmWager = {
        showConfirmWager = null
        viewModel.flipCoin()
      })
  }

  if (showPlayEndAlert == true) {
    PlayEndAlert(
      modifier = Modifier.wrapContentHeight().fillMaxWidth(0.8f)
    ) {
      showPlayEndAlert = null
    }
  }

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
          Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(size_16),
          verticalAlignment = Alignment.CenterVertically
        ) {

//          DefaultButton(
//            Modifier.wrapContentSize(),
//            text = "${state.achievement ?: 0} XP",
//            startIcon = Res.drawable.ic_trophy,
//            padding = PaddingValues(horizontal = size_12, vertical = size_8),
//            contentColor = Color.Black,
//            backgroundImage = Res.drawable.background_button_small
//          ) {
//            navigateToNext(AppNavigation.WeeklyRankingScreen)
//          }

          DefaultTonalButton(
            Modifier.wrapContentSize(),
            text = null,
            startIcon = Res.drawable.ic_wallet,
            padding = PaddingValues(horizontal = size_12, vertical = size_8),
            endIcon = Res.drawable.ic_active,
            endIconTint = AppColors.active
          ) {

          }

          Spacer(modifier = Modifier.weight(1f))

          Text(
            text = state.accountText,
            style = typography.body.LargeSemiBold,
            color = colors.secondary,
            modifier = Modifier.wrapContentWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End
          )
        }

        var selectedTabIndex by rememberMutableStateOf(0)
        val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })

        val tabs = listOf(
          stringResource(Res.string.skr),
          stringResource(Res.string.sol),
          stringResource(Res.string.jup),
        )

        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(pagerState.currentPage) {
          selectedTabIndex = pagerState.currentPage
        }

        TabRow(
          modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = size_12),
          selectedTabIndex = selectedTabIndex,
          containerColor = Color.Transparent,
          contentColor = Color.Black,
          divider = { null },
          indicator = { null }) {
          tabs.forEachIndexed { index, title ->
            Tab(
              modifier = Modifier.wrapContentSize().border(
                2.dp,
                shape = RoundedCornerShape(size_26),
                color = if (selectedTabIndex == index) LocalContentColor.current else Color.Transparent
              ),
              interactionSource = null,
              selected = selectedTabIndex == index,
              onClick = {
                selectedTabIndex = index
                coroutineScope.launch {
                  pagerState.scrollToPage(index)
                }
              },
              text = {
                Text(
                  text = title,
                  style = typography.heading.DefaultSemiBold,
                  color = if (selectedTabIndex == index) LocalContentColor.current else colors.secondary
                )
              },
            )
          }
        }
        HorizontalPager(
          state = pagerState, modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_12)
        ) { page ->

          when (page) {
            0 -> {

            }

            1 -> {
              LazyColumn(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
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
                      if (showConfirmWager != null) {
                        Image(
                          painter = painterResource(Res.drawable.btn_tap_to_flip),
                          contentScale = ContentScale.FillWidth,
                          modifier = Modifier.align(Alignment.BottomCenter).width(220.dp).wrapContentHeight()
                            .padding(horizontal = size_24).clickable { showConfirmWager = true },
                          contentDescription = ""
                        )
                      }
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
                        text = formattedWager,
                        style = typography.heading.DefaultBold,
                        color = colors.primary,
                        modifier = Modifier.padding(start = 2.dp)
                      )
                    }

                    CustomSlider(state.wager) {
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
                  PlayInfoCard(Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_12), it)
                }


              }
            }

            else -> {

            }

          }
        }
      }
    }
  }
}

