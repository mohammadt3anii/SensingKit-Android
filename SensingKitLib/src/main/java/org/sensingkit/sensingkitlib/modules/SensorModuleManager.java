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

package org.sensingkit.sensingkitlib.modules;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.sensingkit.sensingkitlib.SKException;
import org.sensingkit.sensingkitlib.SKExceptionErrorCode;
import org.sensingkit.sensingkitlib.SKSensorDataListener;
import org.sensingkit.sensingkitlib.model.data.DataInterface;


public class SensorModuleManager {

    @SuppressWarnings("unused")
    private static final String TAG = "SensorModuleManager";

    private static final int TOTAL_SENSOR_MODULES = 16;

    private static SensorModuleManager sSensorModuleManager;
    private final Context mApplicationContext;

    private final SparseArray<AbstractSensorModule> mSensors;

    public static SensorModuleManager getSensorManager(final Context context) throws SKException {

        if (context == null) {
            throw new SKException(TAG, "Context cannot be null.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        if (sSensorModuleManager == null) {
            sSensorModuleManager = new SensorModuleManager(context);
        }

        return sSensorModuleManager;
    }

    private SensorModuleManager(final Context context) throws SKException {

        mApplicationContext = context;

        // Init Sensor Array
        mSensors = new SparseArray<>(TOTAL_SENSOR_MODULES);
    }

    public void registerSensorModule(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Register sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        if (isSensorModuleRegistered(moduleType)) {
            throw new SKException(TAG, "SensorModule is already registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Register the SensorModule
        int sensorIndex = moduleType.ordinal();
        AbstractSensorModule sensorModule = createSensorModule(moduleType);
        mSensors.put(sensorIndex, sensorModule);
    }

    public void deregisterSensorModule(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Deregister sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        if (!isSensorModuleRegistered(moduleType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        if (isSensorModuleSensing(moduleType)) {
            throw new SKException(TAG, "SensorModule is currently sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Clear all Callbacks from that sensor
        getSensorModule(moduleType).unsubscribeAllSensorDataListeners();

        // Deregister the SensorModule
        int sensorIndex = moduleType.ordinal();
        mSensors.delete(sensorIndex);
    }

    public boolean isSensorModuleRegistered(SensorModuleType moduleType) throws SKException {

        int sensorIndex = moduleType.ordinal();
        return (mSensors.get(sensorIndex) != null);
    }

    public boolean isSensorModuleSensing(SensorModuleType moduleType) throws SKException {

        if (!isSensorModuleRegistered(moduleType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        return getSensorModule(moduleType).isSensing();
    }

    protected AbstractSensorModule getSensorModule(SensorModuleType moduleType) throws SKException {

        if (!isSensorModuleRegistered(moduleType)) {
            throw new SKException(TAG, "SensorModule is not registered.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        int sensorIndex = moduleType.ordinal();
        return mSensors.get(sensorIndex);
    }

    protected AbstractSensorModule createSensorModule(SensorModuleType moduleType) throws SKException {

        AbstractSensorModule sensorModule;

        switch (moduleType) {

            case ACCELEROMETER:
                sensorModule = new Accelerometer(mApplicationContext);
                break;

            case GRAVITY:
                sensorModule = new Gravity(mApplicationContext);
                break;

            case LINEAR_ACCELERATION:
                sensorModule = new LinearAcceleration(mApplicationContext);
                break;

            case GYROSCOPE:
                sensorModule = new Gyroscope(mApplicationContext);
                break;

            case ROTATION:
                sensorModule = new Rotation(mApplicationContext);
                break;

            case MAGNETOMETER:
                sensorModule = new Magnetometer(mApplicationContext);
                break;

            case AMBIENT_TEMPERATURE:
                sensorModule = new AmbientTemperature(mApplicationContext);
                break;

            case STEP_DETECTOR:
                sensorModule = new StepDetector(mApplicationContext);
                break;

            case STEP_COUNTER:
                sensorModule = new StepCounter(mApplicationContext);
                break;

            case LIGHT:
                sensorModule = new Light(mApplicationContext);
                break;

            case LOCATION:
                sensorModule = new Location(mApplicationContext);
                break;

            case ACTIVITY:
                sensorModule = new Activity(mApplicationContext);
                break;

            case BATTERY:
                sensorModule = new Battery(mApplicationContext);
                break;

            case SCREEN_STATUS:
                sensorModule = new ScreenStatus(mApplicationContext);
                break;

            case AUDIO_RECORDER:
                sensorModule = new AudioRecorder(mApplicationContext);
                break;

            case AUDIO_LEVEL:
                sensorModule = new AudioLevel(mApplicationContext);
                break;

            // Don't forget the break; here

            default:
                throw new SKException(TAG, "Unknown SensorModule", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        return sensorModule;
    }

    public DataInterface getDataFromSensor(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Get data from sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        throw new SKException(TAG, "This feature is not supported just yet!", SKExceptionErrorCode.UNKNOWN_ERROR);
    }

    public void subscribeSensorDataListener(SensorModuleType moduleType, SKSensorDataListener dataListener) throws SKException {

        Log.i(TAG, "Subscribe to sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        getSensorModule(moduleType).subscribeSensorDataListener(dataListener);
    }

    public void unsubscribeSensorDataListener(SensorModuleType moduleType, SKSensorDataListener dataListener) throws SKException {

        Log.i(TAG, "Unsubscribe from sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        getSensorModule(moduleType).unsubscribeSensorDataListener(dataListener);
    }

    public void unsubscribeAllSensorDataListeners(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Unsubscribe from all sensors.");

        getSensorModule(moduleType).unsubscribeAllSensorDataListeners();
    }

    public void startContinuousSensingWithSensor(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Start sensing with sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        if (isSensorModuleSensing(moduleType)) {
            throw new SKException(TAG, "SensorModule is already sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        // Start Sensing
        getSensorModule(moduleType).startSensing();
    }

    public void stopContinuousSensingWithSensor(SensorModuleType moduleType) throws SKException {

        Log.i(TAG, "Stop sensing with sensor: " + SensorModuleUtilities.getSensorModuleInString(moduleType) + ".");

        if (!isSensorModuleSensing(moduleType)) {
            throw new SKException(TAG, "SensorModule is already not sensing.", SKExceptionErrorCode.UNKNOWN_ERROR);
        }

        SensorModuleInterface sensorModule = getSensorModule(moduleType);

        // Stop Sensing
        sensorModule.stopSensing();
    }

}
