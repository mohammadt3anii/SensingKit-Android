/*
 * Copyright (c) 2014. Queen Mary University of London
 * Kleomenis Katevas, k.katevas@qmul.ac.uk
 *
 * This file is part of SensingKit-Android library.
 * For more information, please visit http://www.sensingkit.org
 *
 * SensingKit-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SensingKit-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SensingKit-Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.sensingkit.sensingkitlib;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.sensingkit.sensingkitlib.data.SKSensorData;
import org.sensingkit.sensingkitlib.modules.*;

public class SKSensorManager {

    @SuppressWarnings("unused")
    private static final String TAG = "SKSensorManager";

    private static final int TOTAL_SENSOR_MODULES = 19;

    private static SKSensorManager sSensorManager;
    private final Context mApplicationContext;

    private final SparseArray<SKAbstractSensor> mSensors;

    public static SKSensorManager getSensorManager(final Context context) throws SKException {

        if (context == null) {
            throw new SKException(TAG, "Context cannot be null.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        if (sSensorManager == null) {
            sSensorManager = new SKSensorManager(context);
        }

        return sSensorManager;
    }

    private SKSensorManager(final Context context) throws SKException {

        mApplicationContext = context;

        // Init Sensor Array
        mSensors = new SparseArray<>(TOTAL_SENSOR_MODULES);
    }

    public void registerSensor(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Register sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        if (isSensorRegistered(sensorType)) {
            throw new SKException(TAG, "SensorModule is already registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Register the SensorModule
        int sensorIndex = sensorType.ordinal();
        SKAbstractSensor sensorModule = createSensorModule(sensorType);
        mSensors.put(sensorIndex, sensorModule);
    }

    public void deregisterSensor(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Deregister sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        if (!isSensorRegistered(sensorType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        if (isSensorSensing(sensorType)) {
            throw new SKException(TAG, "SensorModule is currently sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Clear all Callbacks from that sensor
        getSensor(sensorType).unsubscribeAllSensorDataListeners();

        // Deregister the SensorModule
        int sensorIndex = sensorType.ordinal();
        mSensors.delete(sensorIndex);
    }

    public boolean isSensorRegistered(SKSensorType sensorType) throws SKException {

        int sensorIndex = sensorType.ordinal();
        return (mSensors.get(sensorIndex) != null);
    }

    public boolean isSensorSensing(SKSensorType sensorType) throws SKException {

        if (!isSensorRegistered(sensorType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        return getSensor(sensorType).isSensing();
    }

    protected SKAbstractSensor getSensor(SKSensorType sensorType) throws SKException {

        if (!isSensorRegistered(sensorType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        int sensorIndex = sensorType.ordinal();
        return mSensors.get(sensorIndex);
    }

    protected SKAbstractSensor createSensorModule(SKSensorType sensorType) throws SKException {

        SKAbstractSensor sensorModule;

        switch (sensorType) {

            case ACCELEROMETER:
                sensorModule = new SKAccelerometer(mApplicationContext);
                break;

            case GRAVITY:
                sensorModule = new SKGravity(mApplicationContext);
                break;

            case LINEAR_ACCELERATION:
                sensorModule = new SKLinearAcceleration(mApplicationContext);
                break;

            case GYROSCOPE:
                sensorModule = new SKGyroscope(mApplicationContext);
                break;

            case ROTATION:
                sensorModule = new SKRotation(mApplicationContext);
                break;

            case MAGNETOMETER:
                sensorModule = new SKMagnetometer(mApplicationContext);
                break;

            case AMBIENT_TEMPERATURE:
                sensorModule = new SKAmbientTemperature(mApplicationContext);
                break;

            case STEP_DETECTOR:
                sensorModule = new SKStepDetector(mApplicationContext);
                break;

            case STEP_COUNTER:
                sensorModule = new SKStepCounter(mApplicationContext);
                break;

            case LIGHT:
                sensorModule = new SKLight(mApplicationContext);
                break;

            case LOCATION:
                sensorModule = new SKLocation(mApplicationContext);
                break;

            case ACTIVITY:
                sensorModule = new SKActivity(mApplicationContext);
                break;

            case BATTERY:
                sensorModule = new SKBattery(mApplicationContext);
                break;

            case SCREEN_STATUS:
                sensorModule = new SKScreenStatus(mApplicationContext);
                break;

            case AUDIO_RECORDER:
                sensorModule = new SKAudioRecorder(mApplicationContext);
                break;

            case AUDIO_LEVEL:
                sensorModule = new SKAudioLevel(mApplicationContext);
                break;

            case BLUETOOTH:
                sensorModule = new SKBluetooth(mApplicationContext);
                break;

            case HUMIDITY:
                sensorModule = new SKHumidity(mApplicationContext);
                break;

            case AIR_PRESSURE:
                sensorModule = new SKAirPressure(mApplicationContext);
                break;

            // Don't forget the break; here

            default:
                throw new SKException(TAG, "Unknown SensorModule", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        return sensorModule;
    }

    public SKSensorData getDataFromSensor(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Get data from sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        throw new SKException(TAG, "This feature is not supported just yet!", SKExceptionErrorCode.UNKNOWN_ERROR);
    }

    public void subscribeSensorDataListener(SKSensorType sensorType, SKSensorDataListener dataListener) throws SKException {

        Log.i(TAG, "Subscribe to sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        getSensor(sensorType).subscribeSensorDataListener(dataListener);
    }

    public void unsubscribeSensorDataListener(SKSensorType sensorType, SKSensorDataListener dataListener) throws SKException {

        Log.i(TAG, "Unsubscribe from sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        getSensor(sensorType).unsubscribeSensorDataListener(dataListener);
    }

    public void unsubscribeAllSensorDataListeners(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Unsubscribe from all sensors.");

        getSensor(sensorType).unsubscribeAllSensorDataListeners();
    }

    public void startContinuousSensingWithSensor(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Start sensing with sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        if (isSensorSensing(sensorType)) {
            throw new SKException(TAG, "SensorModule is already sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Start Sensing
        getSensor(sensorType).startSensing();
    }

    public void stopContinuousSensingWithSensor(SKSensorType sensorType) throws SKException {

        Log.i(TAG, "Stop sensing with sensor: " + SKSensorModuleUtilities.getSensorModuleInString(sensorType) + ".");

        if (!isSensorSensing(sensorType)) {
            throw new SKException(TAG, "SensorModule is already not sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        SKSensorInterface sensorModule = getSensor(sensorType);

        // Stop Sensing
        sensorModule.stopSensing();
    }

}