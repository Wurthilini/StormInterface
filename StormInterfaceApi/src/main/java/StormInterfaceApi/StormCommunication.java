package StormInterfaceApi;

import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;

import StormInterfaceApi.StormCommunicationManager.DEVICE_INFO;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * <p>Demonstrate the USB HID interface using a Satoshi Labs Trezor</p>
 *
 * @since 0.0.1
 * Â 
 */
public class StormCommunication implements HidServicesListener {
  private static final Integer VENDOR_ID = 0x2047;
  private static final Integer PRODUCT_ID = 0x9d0;
  private static final Integer PACKET_LENGTH = 64;
  //private static final Integer PACKET_LENGTH = 260;
  //private static final Integer VENDOR_ID = 0x534c;
  //private static final Integer PRODUCT_ID = 0x01;
  //private static final int PACKET_LENGTH = 64;
  private static final String SERIAL_NUMBER = null;

  public static void main(String[] args) throws Exception {

    StormCommunication example = new StormCommunication();
    //example.executeExample();
    StormCommunicationManager comunicManager = new StormCommunicationManager();
    boolean test = comunicManager.InitialiseStormUSBDevice();
    DEVICE_INFO deviceInfo = new DEVICE_INFO();
    try {
		deviceInfo = comunicManager.GetDeviceStatus(500);
		System.out.printf("LED LEVEL -> %d \n", deviceInfo.led_brightness);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

	/*public static class DeviceInfo
	{
		byte led_brightness;
		byte keypad_table;
		byte jack_status;
		byte HV_status;
		byte[] keyCode = new byte[20];
		String version;
		String serialNumber;
	}*/
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  @SuppressWarnings("deprecation")
private void executeExample() throws HidException {

    // Configure to use custom specification
    HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
    hidServicesSpecification.setAutoShutdown(true);
    hidServicesSpecification.setScanInterval(500);
    hidServicesSpecification.setPauseInterval(5000);
    hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);

    // Get HID services using custom specification
    HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
    hidServices.addHidServicesListener(this);

    // Start the services
    System.out.println("Starting HID services.");
    hidServices.start();

    System.out.println("Enumerating attached devices...");

    // Provide a list of attached devices
    for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
      System.out.println(hidDevice);
    }

    // Open the device device by Vendor ID and Product ID with wildcard serial number
    HidDevice hidDevice = hidServices.getHidDevice(VENDOR_ID, PRODUCT_ID, SERIAL_NUMBER, (byte) 0x01);
    if (hidDevice != null) {
      // Consider overriding dropReportIdZero on Windows
      // if you see "The parameter is incorrect"
      //HidApi.dropReportIdZero = true;

      // Device is already attached and successfully opened so send message
    	//NOOON BLOCKING MODE?!
      sendMessage(hidDevice);
      GlobalKeyListenerExample test = new GlobalKeyListenerExample();
      test.main(null);
    }
    else
    	System.out.println("No fitting UsbDevice");
    

//    System.out.printf("Waiting 30s to demonstrate attach/detach handling. Watch for slow response after write if configured.%n");

    // Stop the main thread to demonstrate attach and detach events
//    sleepNoInterruption(30, TimeUnit.SECONDS);

    // Shut down and rely on auto-shutdown hook to clear HidApi resources
    hidServices.shutdown();
  }

  @Override
  public void hidDeviceAttached(HidServicesEvent event) {

    System.out.println("Device attached: " + event);

    // Add serial number when more than one device with the same
    // vendor ID and product ID will be present at the same time
    if (event.getHidDevice().isVidPidSerial(VENDOR_ID, PRODUCT_ID, null)) {
      sendMessage(event.getHidDevice());
    }

  }

  @Override
  public void hidDeviceDetached(HidServicesEvent event) {

    System.err.println("Device detached: " + event);

  }

  @Override
  public void hidFailure(HidServicesEvent event) {

    System.err.println("HID failure: " + event);

  }

  private void sendMessage(HidDevice hidDevice) {

    // Ensure device is open after an attach/detach event
    if (!hidDevice.isOpen()) {
      hidDevice.open();
    }
    // Send the Initialise message
byte[] message = new byte[63];
    
    //-> sed led level 0-9
    message[0] = 0x40;
    message[1] = 0x02;
    message[2] = 0x30;
    message[3] = 0x32;
    message[4] = 0x30;
    message[5] = 0x31;
    message[7] = 0x03;
    byte[] ledLevel = new byte[] {(byte) 0x00,(byte) 0x01,(byte) 0x02,(byte) 0x03,(byte) 0x04,
    		(byte) 0x05,(byte) 0x06,(byte) 0x07,(byte) 0x08, (byte) 0x09};
    byte[] fittingLRC = new byte[] {(byte) 0x02,(byte) 0x03,(byte) 0x00,(byte) 0x01,(byte) 0x06,(byte) 0x07
    		,(byte) 0x04,(byte) 0x05,(byte) 0x0a,(byte) 0x0b};
    for(int i=0;i<10;i++)
    {
    	message[6] = ledLevel[i];
    	message[8] = fittingLRC[i];
	    int val = hidDevice.write(message, PACKET_LENGTH, (byte) 0x3f);
	    if (val >= 0) {
	      System.out.println("> [" + val + "]");
	    } else {
	      System.err.println(hidDevice.getLastErrorMessage());
	      return;
	    }
	
	    // Prepare to read a single data packet
	    boolean moreData = true;
	    while (moreData) {
	      byte[] data = new byte[PACKET_LENGTH];
	      // This method will now block for 500ms or until data is read
	      val = hidDevice.read(data, 500);
	      switch (val) {
	        case -1:
	          System.err.println(hidDevice.getLastErrorMessage());
	          break;
	        case 0:
	          moreData = false;
	          break;
	        default:
	          System.out.print("< [");
	          for (byte b : data) {
	            System.out.printf(" %02x", b);
	          }
	          System.out.println("]");
	          break;
	      }
	    }
    }
  }
  
	public byte calculateLRC(byte[] bytes)
	{
		int LRC = 0;
		for (int i = 0; i<bytes.length; i++)
			LRC -= bytes[i];
		return (byte)LRC;
	}

  /**
   * Invokes {@code unit.}{@link java.util.concurrent.TimeUnit#sleep(long) sleep(sleepFor)}
   * uninterruptibly.
   */
  private static void sleepNoInterruption(long sleepFor, TimeUnit unit) {
    boolean interrupted = false;
    try {
      long remainingNanos = unit.toNanos(sleepFor);
      long end = System.nanoTime() + remainingNanos;
      while (true) {
        try {
          // TimeUnit.sleep() treats negative timeouts just like zero.
          NANOSECONDS.sleep(remainingNanos);
          return;
        } catch (InterruptedException e) {
          interrupted = true;
          remainingNanos = end - System.nanoTime();
        }
      }
    } finally {
      if (interrupted) {
        Thread.currentThread().interrupt();
      }
    }
  }

}

