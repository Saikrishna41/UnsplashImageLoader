

package com.task.imageloaderapp.presentation.common
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.task.imageloaderapp.R
import com.task.imageloaderapp.presentation.Dimens


@Composable
fun UnavailableScreen(modifier : Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_network_error),
            contentDescription = "network error icon"
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.TextPadding1)
        )
        Text(text = stringResource(id = R.string.network_issue))
    }
}