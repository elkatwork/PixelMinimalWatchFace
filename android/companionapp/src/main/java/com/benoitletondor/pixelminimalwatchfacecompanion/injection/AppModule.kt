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
package com.benoitletondor.pixelminimalwatchfacecompanion.injection

import com.benoitletondor.pixelminimalwatchfacecompanion.device.Device
import com.benoitletondor.pixelminimalwatchfacecompanion.device.DeviceImpl
import com.benoitletondor.pixelminimalwatchfacecompanion.storage.Storage
import com.benoitletondor.pixelminimalwatchfacecompanion.storage.StorageImpl
import com.benoitletondor.pixelminimalwatchfacecompanion.sync.Sync
import com.benoitletondor.pixelminimalwatchfacecompanion.sync.SyncImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val REMOTE_CONFIG_FETCH_THROTTLE_DEFAULT_VALUE_HOURS = 1L

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonBindingModule {
    @Binds
    @Singleton
    abstract fun bindSync(
        syncImpl: SyncImpl
    ): Sync

    @Binds
    @Singleton
    abstract fun bindStorage(
        storageImpl: StorageImpl
    ): Storage

    @Binds
    @Singleton
    abstract fun bindDevice(
        deviceImpl: DeviceImpl
    ): Device
}