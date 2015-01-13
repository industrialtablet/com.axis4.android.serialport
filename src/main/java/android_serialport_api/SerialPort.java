package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SerialPort {

  private static final String TAG = "SerialPortNative";

  // Do not remove or rename the field mFd: it is used by native method close();
  private FileDescriptor mFd;

  public SerialPort(File device, int baudRate, int flags) throws IOException, SecurityException {
    if (!device.canRead() || !device.canWrite()) {
//      try {
//				// Missing read/write permission, trying to chmod the file
//        Process su;
//        su = Runtime.getRuntime().exec("/system/bin/su");
//        String cmd = "chmod 666 " + device.getAbsolutePath() + "\nexit\n";
//        su.getOutputStream().write(cmd.getBytes());
//        if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
//          throw new SecurityException();
//        }
//      } catch (Exception e) {
//        e.printStackTrace();
      throw new SecurityException();
//      }
    }
    mFd = open(device.getAbsolutePath(), baudRate, flags);
    if (mFd == null) {
      Log.e(TAG, "native open returns null");
      throw new IOException();
    }
  }

  public FileDescriptor getDevice() {
    return mFd;
  }

  // JNI
  private native static FileDescriptor open(String path, int baudrate, int flags);
  public native void close();
  static {
    System.loadLibrary("serial_port");
  }
}
