package core.features.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.common.injecting
import core.designSystem.elements.DefaultButton
import core.designSystem.theme.AppDimensions.size_16
import core.designSystem.theme.AppDimensions.size_20
import core.designSystem.theme.AppDimensions.size_24
import core.designSystem.theme.AppDimensions.size_8
import core.designSystem.theme.AppTheme.colors
import core.designSystem.theme.AppTheme.typography
import core.features.connectWallet.selectWallet.SelectWalletBottomSheet
import core.resources.generated.resources.Res
import core.resources.generated.resources.agree_terms
import core.resources.generated.resources.background_blurred
import core.resources.generated.resources.connect_your_wallet
import core.resources.generated.resources.img_coin_group

import core.resources.generated.resources.terms_of_service
import core.resources.generated.resources.welcome_desc
import core.resources.generated.resources.welcome_intro
import core.ui.navigation.AppNavigation
import core.ui.rememberMutableStateOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
  navigateToNext: (AppNavigation) -> Unit
) {

  val welcomeViewModel by injecting<WelcomeViewModel>()

  var isTermsAndConditionChecked by rememberMutableStateOf(false)
  var showChooseWallet by rememberMutableStateOf(false)

  val onWalletConnected by welcomeViewModel.onWalletConnected.collectAsStateWithLifecycle()

  LaunchedEffect(onWalletConnected) {
    onWalletConnected.actUpOn {
      navigateToNext(AppNavigation.MainScreen)
    }
  }

  if (showChooseWallet) {
    SelectWalletBottomSheet(
      onDismissRequest = { showChooseWallet = false },
      onSelectWallet = {
        showChooseWallet = false
        welcomeViewModel.connect(it)
      }
    )
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets(0.dp)
  ) {

    Image(
      painter = painterResource(Res.drawable.background_blurred),
      contentDescription = "",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    Box(
      Modifier
        .systemBarsPadding()
        .fillMaxSize()
    ) {
      Column(
        modifier = Modifier.fillMaxSize(),
      ) {

        Image(
          painter = painterResource(Res.drawable.img_coin_group),
          contentDescription = "",
          contentScale = ContentScale.Fit,
          modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Spacer(modifier = Modifier.height(size_16))

        Column(
          modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(size_16),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            stringResource(Res.string.welcome_intro),
            textAlign = TextAlign.Center,
            style = typography.display.MediumBold,
          )

          Text(
            stringResource(Res.string.welcome_desc),
            textAlign = TextAlign.Center, modifier = Modifier.padding(top = size_8), color = colors.secondary
          )

          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = size_24).clickable {
            isTermsAndConditionChecked = !isTermsAndConditionChecked
          }) {
            RadioButton(selected = isTermsAndConditionChecked, onClick = {
              isTermsAndConditionChecked = !isTermsAndConditionChecked
            })
            Text(
              buildAnnotatedString {
                append(stringResource(Res.string.agree_terms))
                append(" ")
                withStyle(
                  SpanStyle(
                    textDecoration = TextDecoration.Underline
                  )
                ) {
                  append(stringResource(Res.string.terms_of_service))
                }
              },
              textAlign = TextAlign.Center,
              style = typography.body.DefaultMedium
            )
          }

          DefaultButton(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = size_16, bottom = size_20),
            text = stringResource(Res.string.connect_your_wallet),
            onClick = {
              showChooseWallet = true
//              walletHandler.connect {
//                result = it
//              }
            }
          )
        }

      }
    }
  }
}

