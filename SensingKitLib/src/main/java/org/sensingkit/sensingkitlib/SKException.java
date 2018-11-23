/*
 * Copyright (c) 2014. Kleomenis Katevas
 * Kleomenis Katevas, k.katevas@imperial.ac.uk
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

public class SKException extends Exception {

    private final SKExceptionErrorCode errorCode;
    private final String TAG;

    /**
     *
     * @param TAG - TAG
     * @param message - error message
     * @param errorCode - error code
     */
    public SKException(final String TAG, final String message, final SKExceptionErrorCode errorCode) {
        super(message);
        this.TAG = TAG;
        this.errorCode = errorCode;
    }

    /**
     * Get the error code
     *
     * @return error code
     */
    @SuppressWarnings("unused")
    public SKExceptionErrorCode getErrorCode() {
        return this.errorCode;
    }

    /**
     * Get the tag
     *
     * @return tag
     */
    @SuppressWarnings("unused")
    public String getTAG() {
        return this.TAG;
    }
}
