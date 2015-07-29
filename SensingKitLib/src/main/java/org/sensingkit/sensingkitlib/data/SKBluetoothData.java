/*
 * Copyright (c) 2015. Queen Mary University of London
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

package org.sensingkit.sensingkitlib.data;

import java.util.ArrayList;

public class SKBluetoothData extends SKAbstractData {

    @SuppressWarnings("unused")
    private static final String TAG = "SKBluetoothData";

    private final ArrayList<SKBluetoothDeviceData> mBluetoothDevices;

    public SKBluetoothData(long timestamp, ArrayList<SKBluetoothDeviceData> bluetoothDevices) {

        super(timestamp);

        this.mBluetoothDevices = bluetoothDevices;
    }

    @Override
    public String getDataInCSV() {
        return null;
    }

    @SuppressWarnings("unused")
    public ArrayList<SKBluetoothDeviceData> getBluetoothDevices() {
        return this.mBluetoothDevices;
    }
}