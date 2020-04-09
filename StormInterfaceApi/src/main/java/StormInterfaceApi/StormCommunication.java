package StormInterfaceApi;


import StormInterfaceApi.deviceManager.GlobalKeyListener;
import StormInterfaceApi.deviceManager.Talker;
import StormInterfaceApi.deviceManager.Talker.JACKSTATUS;
import StormInterfaceApi.deviceManager.Talker;
import StormInterfaceApi.utilities.DeviceInfo;

public class StormCommunication{

  public static void main(String[] args) throws Exception {
    StormCommunicationManager comunicManager = new StormCommunicationManager();
    comunicManager.initialiseStormUSBDevice();
	try {
		boolean retbool;
		//retbool = comunicManager.resetToFactoryDefault();
		//retbool = comunicManager.setLedLevel();
		retbool = comunicManager.assignKeypadTable();
	    //retbool = comunicManager.setSerialNumber();
	    retbool = comunicManager.writeDefaultToFlash();
	    Talker textSpeech = new Talker(comunicManager);
	    GlobalKeyListener globalKeyListener = new GlobalKeyListener(textSpeech);
	    globalKeyListener.start();
	   } catch (Exception e) {
		   e.printStackTrace();
	}
  }
}

