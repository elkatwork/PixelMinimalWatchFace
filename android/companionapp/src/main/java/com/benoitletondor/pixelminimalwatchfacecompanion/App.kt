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
package com.benoitletondor.pixelminimalwatchfacecompanion

import android.app.Application
import androidx.lifecycle.*
import com.benoitletondor.pixelminimalwatchfacecompanion.device.Device
import com.benoitletondor.pixelminimalwatchfacecompanion.storage.Storage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver, CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default) {
    @Inject lateinit var storage: Storage
    @Inject lateinit var device: Device

    override fun onCreate() {
        super<Application>.onCreate()

        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(this)

        // Register battery receiver if needed
        if (storage.isBatterySyncActivated()) {
            BatteryStatusBroadcastReceiver.subscribeToUpdates(this)
        }

        if (storage.isForegroundServiceEnabled()) {
            device.startForegroundService()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        cancel()
    }
}