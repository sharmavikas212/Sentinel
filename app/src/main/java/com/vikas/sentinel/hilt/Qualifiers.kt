package com.vikas.sentinel.hilt

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LightSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProximitySensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AccelerometerSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GyroscopeSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PressureSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MagnetometerSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AmbientTemperatureSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HumiditySensorQualifier