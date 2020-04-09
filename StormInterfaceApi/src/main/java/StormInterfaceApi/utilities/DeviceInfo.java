package StormInterfaceApi.utilities;

public class DeviceInfo {
		private int led_brightness;
		private int keypad_table;
		private int jack_status;
		private int HV_status;
		private byte[] keyCode;
		private String version;
		private String serialNumber;
		
		public DeviceInfo()
		{
			this.led_brightness = -1;
			this.keypad_table = -1;
			this.jack_status = -1;
			this.keyCode = new byte[20];
			this.version = "";
			this.serialNumber = "";
		}
		
		public int getLedBrightness()
		{
			return this.led_brightness;
		}
		
		public int getKeypadTable()
		{
			return this.keypad_table;
		}
		
		public boolean getJackStatus()
		{
			boolean jackStatus = this.jack_status == 0 ? true : false;
			return jackStatus;
		}
		
		public int getHvStatus()
		{
			return this.HV_status;
		}
		
		public byte[] getKeyCode()
		{
			return this.keyCode;
		}
		
		public String getVersion()
		{
			return this.version;
		}
		
		public String getSerialNumber()
		{
			return this.serialNumber;
		}
		
		public void setLedBrightness(int ledBrightness)
		{
			this.led_brightness = ledBrightness;
		}
		
		public void setKeypadTable(int keypadTable)
		{
			this.keypad_table = keypadTable;
		}
		
		public void setJackStatus(int jackStatus)
		{
			this.jack_status = jackStatus;
		}
		
		public void setHvStatus(int hvStatus)
		{
			this.HV_status = hvStatus;
		}
		
		public void setKeyCode(byte[] keyCode)
		{
			this.keyCode = keyCode;
		}
		
		public void setVersion(String version)
		{
			this.version = version;
		}
		
		public void setSerialNumber(String serialNo)
		{
			this.serialNumber = serialNo;
		}
	}
