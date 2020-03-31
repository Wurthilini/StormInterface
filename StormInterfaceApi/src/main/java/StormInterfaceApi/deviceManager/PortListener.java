package StormInterfaceApi.deviceManager;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.TimeUnit;

import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesListener;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;
import org.hid4java.event.HidServicesEvent;

import StormInterfaceApi.StormCommunicationManager;

public class PortListener extends StormCommunicationManager implements HidServicesListener{

	private final long sleepFor;
	private HidServices hidServices;
	private HidDevice hidDevice = null;
	
	public PortListener(long sleepFor) throws Exception
	{
		this.sleepFor = sleepFor;
		HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
		hidServicesSpecification.setAutoShutdown(true);
		hidServicesSpecification.setScanInterval(500);
		hidServicesSpecification.setPauseInterval(5000);
		hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);
		this.hidServices = HidManager.getHidServices(hidServicesSpecification);
        this.hidServices.addHidServicesListener(this);
		this.hidServices.start();
		sleepUninterruptibly();
	}

	@Override
	public void hidDeviceAttached(HidServicesEvent e) {;
        if(e.getHidDevice().isVidPidSerial(STORM_VENDOR_ID, STORM_PRODUCT_ID, STORM_SERIAL_NUMBER))
        	this.hidDevice = super.init(this.hidServices, this.hidDevice);
	}

	@Override
	public void hidDeviceDetached(HidServicesEvent e) {
		System.out.println("Device detached");
	}

	@Override
	public void hidFailure(HidServicesEvent e) {
		System.err.print("HID FAILURE");		
	}
	
    private void sleepUninterruptibly() {
    	boolean interrupted = false;
    	TimeUnit unit = TimeUnit.SECONDS;
        try {
            long remainingNanos = unit.toNanos(this.sleepFor);
            long end = System.nanoTime() + remainingNanos;
            while (true) {
                try {
                	if(this.hidDevice!=null)
                		 return;
                	else
                		NANOSECONDS.sleep(unit.toNanos(this.sleepFor));
                } catch (InterruptedException e) {
                	interrupted = true;
                    remainingNanos = end - System.nanoTime();
                }
            }
        } finally {
        	if(interrupted)
        		Thread.currentThread().interrupt();
        }
    }
    
    public HidDevice getHidDevice()
    {
    	return this.hidDevice;
    }
	
	
}
