
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat;

import com.android.chimpchat.adb.AdbBackend;
import com.android.chimpchat.core.IChimpBackend;
import com.android.chimpchat.core.IChimpDevice;

import java.util.Map;
import java.util.TreeMap;

/**
 * ChimpChat is a host-side library that provides an API for communication with
 * an instance of Monkey on a device. This class provides an entry point to
 * setting up communication with a device. Currently it only supports communciation
 * over ADB, however.
 */
public class ChimpChat {
    private final IChimpBackend mBackend;
    private static String sAdbLocation;

    private ChimpChat(IChimpBackend backend) {
        this.mBackend = backend;
    }

    /**
     * Generates a new instance of ChimpChat based on the options passed.
     * @param options a map of settings for the new ChimpChat instance
     * @return a new instance of ChimpChat or null if errors occur during creation
     */
    public static ChimpChat getInstance(Map<String, String> options) {
        sAdbLocation = options.get("adbLocation");
        IChimpBackend backend = createBackendByName(options.get("backend"));
        if (backend == null) {
            return null;
        }
        ChimpChat chimpchat = new ChimpChat(backend);
        return chimpchat;
    }

    /** Generates a new instance of ChimpChat using default settings
     * @return a new instance of ChimpChat or null if errors occur during creation
     */
    public static ChimpChat getInstance() {
        Map<String, String> options = new TreeMap<String, String>();
        options.put("backend", "adb");
        return ChimpChat.getInstance(options);
    }


    /**
     * Creates a specific backend by name.
     *
     * @param backendName the name of the backend to create
     * @return the new backend, or null if none were found.
     */

    private static IChimpBackend createBackendByName(String backendName) {
        if ("adb".equals(backendName)) {
            if (sAdbLocation == null) {
                return new AdbBackend();
            } else {
                return new AdbBackend(sAdbLocation);
            }
        } else {
            return null;
        }
    }

    /**
     * Retrieves an instance of the device from the backend
     * @param timeoutMs length of time to wait before timing out
     * @param deviceId the id of the device you want to connect to
     * @return an instance of the device
     */
    public IChimpDevice waitForConnection(long timeoutMs, String deviceId){
        return mBackend.waitForConnection(timeoutMs, deviceId);
    }

    /**
     * Retrieves an instance of the device from the backend.
     * Defaults to the longest possible wait time and any available device.
     * @return an instance of the device
     */
    public IChimpDevice waitForConnection(){
        return mBackend.waitForConnection(Integer.MAX_VALUE, ".*");
    }

    /**
     * Shutdown and clean up chimpchat.
     */
    public void shutdown(){
        mBackend.shutdown();
    }
}
