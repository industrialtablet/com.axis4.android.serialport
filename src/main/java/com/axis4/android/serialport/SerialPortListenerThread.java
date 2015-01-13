/*
 * Copyright 2014 Maxime Boulay-Cote
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

import java.io.IOException;
import java.io.InputStream;

class SerialPortListenerThread extends Thread {

  private InputStream mInputStream;
  private SerialPortListener mSerialPortListener;

  public SerialPortListenerThread(SerialPort serialPort, SerialPortListener serialPortListener) {
    mInputStream = serialPort.getInputStream();
    mSerialPortListener = serialPortListener;
  }

  @Override
  public void run() {
    super.run();
    while (!isInterrupted()) {
      int size;
      try {
        byte[] buffer = new byte[64];
        if (mInputStream == null) return;
        size = mInputStream.read(buffer);
        if (size > 0 && mSerialPortListener != null) {
          mSerialPortListener.onDataReceived(buffer, size);
        }
      } catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }
  }
}
