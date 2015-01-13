/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.axis4.android.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Vector;

public class SerialPortFinder {

  public class Driver {
    public Driver(String name, String root) {
      mDriverName = name;
      mDeviceRoot = root;
    }
    private String mDriverName;
    private String mDeviceRoot;
    Vector<File> mDevices = null;
    public Vector<File> getDevices() {
      if (mDevices == null) {
        mDevices = new Vector<>();
        File dev = new File("/dev");
        File[] files = dev.listFiles();
        int i;
        for (i=0; i<files.length; i++) {
          if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
            Log.d(TAG, "Found new device: " + files[i]);
            mDevices.add(files[i]);
          }
        }
      }
      return mDevices;
    }
    public String getName() {
      return mDriverName;
    }
  }

  private static final String TAG = "SerialPort";

  private Vector<Driver> mDrivers = null;

  Vector<Driver> getDrivers() throws IOException {
    if (mDrivers == null) {
      mDrivers = new Vector<>();
      LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));
      String l;
      while((l = r.readLine()) != null) {
        // Driver name may contain spaces, we do not extract driver name with split()
        String name = l.substring(0, 0x15).trim();
        String[] w = l.split(" +");
        if ((w.length >= 5) && (w[w.length-1].equals("serial"))) {
          Log.d(TAG, "Found new driver " + name + " on " + w[w.length-4]);
          mDrivers.add(new Driver(name, w[w.length-4]));
        }
      }
      r.close();
    }
    return mDrivers;
  }

  public String[] getAllDevices() {
    Vector<String> devices = new Vector<>();

    try {
      Vector<Driver> drivers = getDrivers();
      for (Driver driver : drivers) {
        for (File file : driver.getDevices()) {
          String device = file.getName();
          String value = String.format("%s (%s)", device, driver.getName());
          devices.add(value);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return devices.toArray(new String[devices.size()]);
  }

  public String[] getAllDevicesPath() {
    Vector<String> devices = new Vector<>();

    try {
      Vector<Driver> drivers = getDrivers();
      for (Driver driver : drivers) {
        for (File file : driver.getDevices()) {
          String device = file.getAbsolutePath();
          devices.add(device);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return devices.toArray(new String[devices.size()]);
  }
}
