package StormInterfaceApi.messageHandler;

import StormInterfaceApi.utilities.ErrorTypes.ErrorCodeMessageRequest;

public class MessageRequest {

	private int requestType;
	private Object[] additionalParams = new Object[8];
	private byte[] ptrString;
	private String paramStr;
	private ErrorCodeMessageRequest lastErrorCode;
	
	public ErrorCodeMessageRequest getLastErrorCode()
	{
		return this.lastErrorCode;
	}
	
	public int getRequestType()
	{
		return this.requestType;
	}
	
	public Object[] getAdditionalParams()
	{
		return this.additionalParams;
	}
	
	public byte[] getPtrString()
	{
		return this.ptrString;
	}
	
	public String getParamStr()
	{
		return this.paramStr;
	}
	
	public void setRequestType(int reqType)
	{
		this.requestType = reqType;
	}
	
	public void setParam1(byte param1)
	{
		this.additionalParams[0] = new Byte(param1);
	}
	
	public void setParam2(int param2)
	{
		this.additionalParams[1] = new Integer(param2);
	}
	
	public void setParam3(int param3)
	{
		this.additionalParams[2] = new Integer(param3);
	}
	
	public void setParam4(int param4)
	{
		this.additionalParams[3] = new Integer(param4);
	}
	
	public void setParam5(int param5)
	{
		this.additionalParams[4] = new Integer(param5);
	}
	
	public void setParam6(int param6)
	{
		this.additionalParams[5] = new Integer(param6);
	}
	
	public void setParam7(int param7)
	{
		this.additionalParams[6] = new Integer(param7);
	}
	
	public void setParam8(int param8)
	{
		this.additionalParams[7] = new Integer(param8);
	}
	
	public void setPtrString(byte[] ptrString)
	{
		this.ptrString = ptrString;
	}
	
	public void setParamStr(String paramStr)
	{
		this.paramStr = paramStr;
	}
}
	
	
