package StormInterfaceApi.deviceManager;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import StormInterfaceApi.deviceManager.Talker.JACKSTATUS;


public class GlobalKeyListener implements NativeKeyListener{
	private Talker talker;
	private final byte JACKIN = 0x005D; //VK F15
	private final byte JACKOUT = 0x0063; //VK F16
	
	public GlobalKeyListener(Talker talker)
	{
		this.talker = talker;
	}
	
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if(e.getKeyCode()==JACKIN)
			this.talker.setTalkerJackStatus(JACKSTATUS.ON);
		if(e.getKeyCode()==JACKOUT)
			this.talker.setTalkerJackStatus(JACKSTATUS.OFF);
		try {
			this.talker.talk(NativeKeyEvent.getKeyText(e.getKeyCode()));
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			try {
				GlobalScreen.unregisterNativeHook();
				this.talker.closeTalker();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
	}

	@SuppressWarnings("static-access")
	@Override 
	public void nativeKeyTyped(NativeKeyEvent e) {
		System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
	}
	
	public void start()
	{
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new GlobalKeyListener(this.talker));
	}	
}
