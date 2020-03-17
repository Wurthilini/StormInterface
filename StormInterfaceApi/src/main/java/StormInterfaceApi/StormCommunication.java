package StormInterfaceApi;


import StormInterfaceApi.utilities.DeviceInfo;

/**
 * <p>Demonstrate the USB HID interface using a Satoshi Labs Trezor</p>
 *
 * @since 0.0.1
 * Â 
 */
public class StormCommunication{
  /*private static final Integer VENDOR_ID = 0x2047;
  private static final Integer PRODUCT_ID = 0x9d0;
  private static final Integer PACKET_LENGTH = 64;
  private static final String SERIAL_NUMBER = null;*/

  public static void main(String[] args) throws Exception {

    StormCommunicationManager comunicManager = new StormCommunicationManager();
    boolean test = comunicManager.initialiseStormUSBDevice();
   // DEVICE_INFO deviceInfo = new DEVICE_INFO();
    DeviceInfo deviceInfo = new DeviceInfo();
    try {
    	for(int i=0;i<10;i++)
    	{
    		System.out.printf("set led level to -> %d \n", i);
    		boolean test2 = comunicManager.setLedLevel(i);
    		comunicManager.getDeviceStatus(deviceInfo);
    		System.out.printf("LED LEVEL INFO -> %d \n", deviceInfo.getLedBrightness());
    	}
    	byte[] new_table={0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x10, 0x00, 0x11, 0x00, 0x12, 0x00, 0x13};
    	boolean test4 = comunicManager.setKeypadTable(2);
    	boolean test3 = comunicManager.loadCodeTable(new_table, 20);
    	boolean test5 = comunicManager.setSerialNumber("200187654321");
    	boolean test2 = comunicManager.writeDefaultToFlash();
    	comunicManager.getDeviceStatus(deviceInfo);
    	System.out.println(deviceInfo.getSerialNumber());
    	System.out.println(deviceInfo.getVersion());
    	for(byte bytes : deviceInfo.getKeyCode())
    		System.out.printf("%02x ", bytes);
    	boolean test6 = comunicManager.resetToFactoryDefault();
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}

