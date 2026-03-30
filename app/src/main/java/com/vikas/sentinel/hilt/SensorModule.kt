package com.vikas.sentinel.hilt

import android.content.Context
import com.vikas.sentinel.data.repository.SensorRepositoryImpl
import com.vikas.sentinel.data.sensor.AccelerometerSensor
import com.vikas.sentinel.data.sensor.AmbientTemperatureSensor
import com.vikas.sentinel.data.sensor.BatteryPercentage
import com.vikas.sentinel.data.sensor.GyroscopeSensor
import com.vikas.sentinel.data.sensor.HumiditySensor
import com.vikas.sentinel.data.sensor.LightSensors
import com.vikas.sentinel.data.sensor.MagnetometerSensor
import com.vikas.sentinel.domain.model.MeasurableSensor
import com.vikas.sentinel.data.sensor.PressureSensor
import com.vikas.sentinel.data.sensor.ProximitySensor
import com.vikas.sentinel.domain.repository.SensorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SensorModule {

    @Binds
    abstract fun bindSensorRepository(
        impl: SensorRepositoryImpl
    ): SensorRepository

    companion object {
        @Provides
        @Singleton
        @LightSensorQualifier
        fun provideLightSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return LightSensors(appContext)
        }

        @Provides
        @Singleton
        @ProximitySensorQualifier
        fun provideProximitySensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return ProximitySensor(appContext)
        }

        @Provides
        @Singleton
        @AccelerometerSensorQualifier
        fun provideAccelerometerSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return AccelerometerSensor(appContext)
        }

        @Provides
        @Singleton
        @GyroscopeSensorQualifier
        fun provideGyroscopeSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return GyroscopeSensor(appContext)
        }

        @Provides
        @Singleton
        @PressureSensorQualifier
        fun providePressureSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return PressureSensor(appContext)
        }

        @Provides
        @Singleton
        @MagnetometerSensorQualifier
        fun provideMagnetometerSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return MagnetometerSensor(appContext)
        }

        @Provides
        @Singleton
        @AmbientTemperatureSensorQualifier
        fun provideAmbientTemperatureSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return AmbientTemperatureSensor(appContext)
        }

        @Provides
        @Singleton
        @HumiditySensorQualifier
        fun provideHumiditySensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return HumiditySensor(appContext)
        }

        @Provides
        @Singleton
        @BatteryPercentageSensorQualifier
        fun provideBatteryPercentageSensor(@ApplicationContext appContext: Context): MeasurableSensor {
            return BatteryPercentage(appContext)
        }
    }
}