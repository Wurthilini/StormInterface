package StormInterfaceApi;


import StormInterfaceApi.deviceManager.GlobalKeyListener;
import StormInterfaceApi.deviceManager.Talker;


public class StormCommunication{

  public static void main(String[] args) throws Exception {
    StormCommunicationManager communicationManager = new StormCommunicationManager();
    communicationManager.initialiseStormUSBDevice();
	try {
		communicationManager.resetToFactoryDefault();
		communicationManager.setLedLevel();
		communicationManager.assignKeypadTable();
	    communicationManager.setSerialNumber();
	    communicationManager.writeDefaultToFlash();
	    Talker textSpeech = new Talker(communicationManager);
	    GlobalKeyListener globalKeyListener = new GlobalKeyListener(textSpeech);
	    globalKeyListener.start();
	   } catch (Exception e) {
		   e.printStackTrace();
	}
  }
}

