package StormInterfaceApi;


import StormInterfaceApi.deviceManager.GlobalKeyListener;
import StormInterfaceApi.utilities.DeviceInfo;

public class StormCommunication{

  public static void main(String[] args) throws Exception {
    StormCommunicationManager comunicManager = new StormCommunicationManager();
    boolean retbool = comunicManager.initialiseStormUSBDevice();
    if(retbool)
    {
	    DeviceInfo deviceInfo = new DeviceInfo();
	    try {
	    	for(int i=0;i<10;i++)
	    	{
	    		System.out.printf("set led level to -> %d \n", i);
	    		retbool = comunicManager.setLedLevel(i);
	    	}
	    	//byte[] new_table={0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x10, 0x00, 0x11, 0x00, 0x12, 0x00, 0x13};
	    	retbool = comunicManager.setKeypadTable(2);
	    	//retbool = comunicManager.loadCodeTable(new_table, 20);
	    	retbool = comunicManager.setSerialNumber("200187654321");
	    	retbool = comunicManager.writeDefaultToFlash();
	    	comunicManager.getDeviceStatus(deviceInfo);
	    	System.out.println(deviceInfo.getSerialNumber());
	    	System.out.println(deviceInfo.getVersion());
	    	for(byte bytes : deviceInfo.getKeyCode())
	    		System.out.printf("%02x ", bytes);
	    	//retbool = comunicManager.resetToFactoryDefault();
	    	GlobalKeyListener.main(null);
	    	
	    } catch (Exception e) {
			e.printStackTrace();
		}
    }
  }
}

