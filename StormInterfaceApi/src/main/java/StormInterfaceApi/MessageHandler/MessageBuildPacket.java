package StormInterfaceApi.MessageHandler;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageBuildPacket {
	
	private static final byte SC_STX = 0x02;
	private static final byte SC_ETX = 0x03;
	private static final byte USB_REPORT_LEN = 64;
	private static final byte KEYMAT_REPORT_ID = 0x3f;
	
	public byte[] makePacket(int requestType, ArrayList<Byte> dataTosend, byte[] _responseMessage)
	{
		byte[] _responseMessageTemp = new byte[USB_REPORT_LEN];
		byte LRC = SC_STX;
		String messageID = null;
		String messageLength = null;
		char[] convString = {(char) (requestType & 0xff)}; 
		messageID = binaryToASCIIHex(convString);
		int dataTosendLength = dataTosend.isEmpty() ? 0 : dataTosend.size();
		char[] convStringSecond = {(char) (dataTosendLength & 0xff)};
		messageLength = binaryToASCIIHex(convStringSecond);
		_responseMessageTemp[0] = KEYMAT_REPORT_ID;// -> is set in hid_write function
		_responseMessageTemp[1] = USB_REPORT_LEN;
		_responseMessageTemp[2] = SC_STX;
		_responseMessageTemp[3] = (byte) messageID.toCharArray()[0];
		_responseMessageTemp[4] = (byte) messageID.toCharArray()[1];
		_responseMessageTemp[5] = (byte) messageLength.toCharArray()[0];
		_responseMessageTemp[6] = (byte) messageLength.toCharArray()[1];
		int currentIndex = 7;
		for(byte bytesTosend : dataTosend)
		{
			_responseMessageTemp[currentIndex] = bytesTosend;
			currentIndex++;
		}
		_responseMessageTemp[currentIndex] = SC_ETX;
		currentIndex++;
		//calculate LRC
		//skip header, start with SC_STX
		int LRCIndex = 1;
		LRC = _responseMessageTemp[LRCIndex];
		while(LRCIndex < currentIndex)
		{
			LRC ^= _responseMessageTemp[LRCIndex];
			LRCIndex++;
		}
		_responseMessageTemp[currentIndex] = LRC;
		_responseMessage = Arrays.copyOfRange(_responseMessageTemp, 1, _responseMessageTemp.length);
		return _responseMessage;
	}
	
	private String binaryToASCIIHex(char[] binaryData)
	{
		char binaryByte;
		char workNybble;
		String ASCIIData = "";
		for(char binaryChar : binaryData)
		{
			binaryByte = binaryChar;
			//Do upper nybble first
			workNybble = (char) (binaryByte>>4);
			if(workNybble<10)
				workNybble += '0';
			else
				workNybble += 'A'-10;
			ASCIIData += workNybble;
			//Do lower nybble now
			workNybble = (char) (binaryByte & 0x0f);
			if(workNybble <10)
				workNybble += '0';
			else
				workNybble += 'A' -10;
			ASCIIData += workNybble;
		}
		return ASCIIData;
	}
}
