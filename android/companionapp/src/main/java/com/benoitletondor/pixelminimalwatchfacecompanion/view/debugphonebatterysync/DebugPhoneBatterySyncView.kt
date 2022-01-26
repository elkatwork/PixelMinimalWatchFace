/*
 *   Copyright 2022 Benoit LETONDOR
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.benoitletondor.pixelminimalwatchfacecompanion.view.debugphonebatterysync

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.benoitletondor.pixelminimalwatchfacecompanion.ui.AppMaterialTheme
import com.benoitletondor.pixelminimalwatchfacecompanion.ui.blueButtonColors
import com.benoitletondor.pixelminimalwatchfacecompanion.ui.components.AppTopBarScaffold

@Composable
fun DebugPhoneBatterySync(
    navController: NavController,
    viewModel: DebugPhoneBatterySyncViewModel,
) {
    val state: DebugPhoneBatterySyncViewModel.State
        by viewModel.stateFlow.collectAsState()

    val context = LocalContext.current

    val batteryOptimizationOptOutActivityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.onBatteryOptimizationOptOutResult()
    }

    LaunchedEffect("events") {
        viewModel.eventLiveFlow.collect { event ->
            when(event) {
                DebugPhoneBatterySyncViewModel.Event.NavigateToDisableOptimizationActivity -> {
                    val intents = viewModel.device.getBatteryOptimizationOptOutIntents()
                    for(intent in intents) {
                        val resolveInfo = intent.resolveActivityInfo(context.packageManager, PackageManager.MATCH_DEFAULT_ONLY)
                        if (resolveInfo?.exported == true) {
                            batteryOptimizationOptOutActivityResultLauncher.launch(intent)
                            break
                        }
                    }
                }
            }
        }
    }

    AppTopBarScaffold(
        navController = navController,
        showBackButton = true,
        title = "Debug phone battery sync",
        content = {
            DebugPhoneBatterySyncLayout(
                isBatteryOptimizationOff = state.isBatteryOptimizationOff,
                onDisableBatteryOptimizationButtonPressed = viewModel::onDisableBatteryOptimizationButtonPressed
            )
        }
    )
}

@Composable
private fun DebugPhoneBatterySyncLayout(
    isBatteryOptimizationOff: Boolean,
    onDisableBatteryOptimizationButtonPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Disable battery optimization",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isBatteryOptimizationOff) {
            Text(
                text = "✔️ Battery optimization is off.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onBackground,
            )
        } else {
            Text(
                text = "The majority of phone battery sync issues can be resolved by disabling battery optimization for the companion app, so that the system doesn't kill the app in background.",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onBackground,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Search for Pixel Minimal Watch Face and disable battery optimization.",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onBackground,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDisableBatteryOptimizationButtonPressed,
                colors = blueButtonColors(),
            ) {
                Text(text = "Disable battery optimization".uppercase())
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Aggressive always-on mode",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onBackground,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "If you still experience sync issues, you can try to activate always-on mode.",
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "It will use a permanent notification to prevent the system from killing the app.",
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onBackground,
        )
    }
}

@Composable
@Preview(showSystemUi = true, name = "Battery optimization off")
private fun PreviewBatteryOptimOff() {
    AppMaterialTheme {
        DebugPhoneBatterySyncLayout(
            isBatteryOptimizationOff = true,
            onDisableBatteryOptimizationButtonPressed = {},
        )
    }
}

@Composable
@Preview(showSystemUi = true, name = "Battery optimization on")
private fun PreviewBatteryOptimOn() {
    AppMaterialTheme {
        DebugPhoneBatterySyncLayout(
            isBatteryOptimizationOff = false,
            onDisableBatteryOptimizationButtonPressed = {},
        )
    }
}