package StormInterfaceApi;


import StormInterfaceApi.deviceManager.GlobalKeyListener;
import StormInterfaceApi.deviceManager.Talker;
import StormInterfaceApi.utilities.CustomisedCodeTable;
import StormInterfaceApi.utilities.DeviceInfo;
import StormInterfaceApi.utilities.UsbKeyCodes;

public class StormCommunication{

  public static void main(String[] args) throws Exception {
	//Talker talker = new Talker();
	//talker.playSound("../StormInterfaceApi/audio/Cartoon-10.wav");
    StormCommunicationManager comunicManager = new StormCommunicationManager();
    comunicManager.initialiseStormUSBDevice();
	DeviceInfo deviceInfo = new DeviceInfo();
	try {
		boolean retbool;
		retbool = comunicManager.setLedLevel();
		retbool = comunicManager.assignKeypadTable();
	    retbool = comunicManager.setSerialNumber();
	    retbool = comunicManager.writeDefaultToFlash();
	    comunicManager.getDeviceStatus(deviceInfo);
	    System.out.println(deviceInfo.getSerialNumber());
	    System.out.println(deviceInfo.getVersion());
	    System.out.println(deviceInfo.getJackStatus());
	    for(byte bytes : deviceInfo.getKeyCode())
	    	System.out.printf("%02x ", bytes);
	    //retbool = comunicManager.resetToFactoryDefault();
	    GlobalKeyListener.main(null);	
	   } catch (Exception e) {
		   e.printStackTrace();
	}
  }
}

