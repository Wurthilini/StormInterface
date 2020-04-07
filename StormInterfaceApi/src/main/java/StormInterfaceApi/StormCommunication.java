package StormInterfaceApi;


import StormInterfaceApi.deviceManager.GlobalKeyListener;
import StormInterfaceApi.deviceManager.Talker;
import StormInterfaceApi.deviceManager.Talker;
import StormInterfaceApi.utilities.DeviceInfo;

public class StormCommunication{

  public static void main(String[] args) throws Exception {
    StormCommunicationManager comunicManager = new StormCommunicationManager();
    comunicManager.initialiseStormUSBDevice();
	/*try {
		Talker talker = new Talker();
	} catch (Throwable e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}*/
	//Talker textSpeech = new Talker("Right");
	//DeviceInfo deviceInfo = new DeviceInfo();
	try {
		boolean retbool;
		//retbool = comunicManager.resetToFactoryDefault();
		retbool = comunicManager.setLedLevel();
		//retbool = comunicManager.assignKeypadTable();
	    //retbool = comunicManager.setSerialNumber();
	    //retbool = comunicManager.writeDefaultToFlash();
	    //comunicManager.getDeviceStatus(deviceInfo);
	    /*System.out.println(deviceInfo.getSerialNumber());
	    System.out.println(deviceInfo.getVersion());
	    System.out.println(deviceInfo.getJackStatus());
	    for(byte bytes : deviceInfo.getKeyCode())
	    	System.out.printf("%02x ", bytes);*/
	    //retbool = comunicManager.resetToFactoryDefault();
	    GlobalKeyListener.main(null);	
	   } catch (Exception e) {
		   e.printStackTrace();
	}
  }
}

