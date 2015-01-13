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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

  private final static String TAG = "SerialPort";

  private FileInputStream mFileInputStream;
  private FileOutputStream mFileOutputStream;
  private SerialPortListenerThread mSerialPortListenerThread;

  public SerialPort(File device, int baudRate, int flags) throws SecurityException, IOException {
    android_serialport_api.SerialPort serialPortNative = new android_serialport_api.SerialPort(device, baudRate, flags);

    mFileInputStream = new FileInputStream(serialPortNative.getDevice());
    mFileOutputStream = new FileOutputStream(serialPortNative.getDevice());
  }

  public void setListener(SerialPortListener serialPortListener) {
    mSerialPortListenerThread = new SerialPortListenerThread(this, serialPortListener);
    mSerialPortListenerThread.start();
  }

  public void send(String message) {
    byte b = (byte)Integer.parseInt(message);
    send(b);
  }

  public void send(byte b) {
    send(new byte[] {b});
  }

  public void send(byte[] bytes) {
    try {
      mFileOutputStream.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    mSerialPortListenerThread.interrupt();
    mSerialPortListenerThread = null;
  }

  // Getters and setters
  InputStream getInputStream() {
    return mFileInputStream;
  }
  OutputStream getOutputStream() {
    return mFileOutputStream;
  }
}
