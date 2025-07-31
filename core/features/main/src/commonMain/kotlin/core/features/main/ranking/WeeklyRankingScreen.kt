package core.features.WeeklyRanking.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import core.designSystem.elements.BUTTON_SHAPE
import core.designSystem.theme.AppDimensions.size_12
import core.designSystem.theme.AppDimensions.size_14
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppTextSize.size_20
import core.designSystem.theme.AppTheme.typography
import core.features.main.cards.RankingCard
import core.resources.generated.resources.Res
import core.resources.generated.resources.background_blurred_light
import core.resources.generated.resources.background_button
import core.resources.generated.resources.ic_back
import core.resources.generated.resources.weekly_ranking
import core.resources.generated.resources.your_rank
import core.ui.navigation.AppNavigation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyRankingScreen(
  viewModel: WeeklyRankingScreenViewModel, navigateToNext: (AppNavigation) -> Unit
) {
  val state by viewModel.state.collectAsState()
  val playList by viewModel.playList.collectAsState()

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(
            stringResource(Res.string.weekly_ranking),
            style = typography.heading.LargeSemiBold,
            fontSize = size_20,
          )
        },
        navigationIcon = {
          Image(
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "Close",
            modifier = Modifier
              .wrapContentSize()
              .clickable { navigateToNext(AppNavigation.PopBack) },
            contentScale = ContentScale.Fit
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
        modifier = Modifier.padding(horizontal = size_16)
      )
    }
  ) {
    Box(Modifier.fillMaxSize()) {
      Image(
        painter = painterResource(Res.drawable.background_blurred_light),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
      Column(
        modifier = Modifier.fillMaxSize().padding(it).padding(size_16),
      ) {
        Box(
          modifier = Modifier.wrapContentSize().clip(BUTTON_SHAPE).align(Alignment.CenterHorizontally),
        ) {
          Image(
            painter = painterResource(Res.drawable.background_button),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
          )
          Text(
            stringResource(Res.string.your_rank) + " #27",
            modifier = Modifier.padding(2.dp).clip(BUTTON_SHAPE).background(Color.White)
              .padding(vertical = size_14, horizontal = size_12)
          )
        }

        LazyColumn(
          modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_12),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          itemsIndexed(playList) { index, play ->
            RankingCard(Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_12), index + 1, play)
          }


        }
      }


    }
  }
}
