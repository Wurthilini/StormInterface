package StormInterfaceApi.utilities;

import StormInterfaceApi.StormCommunicationManager;

public class CustomisedCodeTable extends StormCommunicationManager{
	private byte[] Volume;
	private byte[] Left;
	private byte[] Right;
	private byte[] Down;
	private byte[] Up;
	private byte[] Enter;
	private byte[] Horizontal;
	private byte[] Vertical;
	private byte[] JackIn;
	private byte[] JackOut;
	private static byte[] codeTable;
	
	public CustomisedCodeTable(int codeTable) throws Exception
	{
		switch(codeTable)
		{
		case 0:
			this.Volume = new byte[] {0x6c};
			this.Left = new byte[] {0x50};
			this.Right = new byte[] {0x4f};
			this.Down = new byte[] {0x51};
			this.Up = new byte[] {0x52};
			this.Enter = new byte[] {0x28};
			this.Horizontal = new byte[] {0x6d};
			this.Vertical = new byte[] {0x6e};
			this.JackIn = new byte[] {0x6a};
			this.JackOut = new byte[] {0x6b};
			setKeypadTable(0);
			break;
		case 1:
			this.Volume = new byte[] {0x6c};
			this.Left = new byte[] {0x50};
			this.Right = new byte[] {0x4f};
			this.Down = new byte[] {0x51};
			this.Up = new byte[] {0x52};
			this.Enter = new byte[] {0x28};
			this.Horizontal = new byte[] {0x6d};
			this.Vertical = new byte[] {0x6e};
			this.JackIn = new byte[] {0x6a};
			this.JackOut = new byte[] {0x6b};
			setKeypadTable(1);
			break;
		default:
			CustomisedCodeTable.codeTable = new byte[20];
			setKeypadTable(2);
			break;
		}
	}
	
	public void loadCodeTable() throws Exception
	{
		//only necassary if customised table 
		if(this.Volume == null)
			setVolume((byte) 0x6c);
		if(this.Left==null)
			setLeft((byte) 0x50);
		if(this.Right==null)
			setRight((byte) 0x4f);
		if(this.Up==null)
			setUp((byte) 0x52);
		if(this.Down==null)
			setDown((byte) 0x51);
		if(this.Enter==null)
			setEnter((byte) 0x28);
		if(this.Horizontal==null)
			setHorizontal((byte) 0x6d);
		if(this.Vertical==null)
			setVertical((byte) 0x6e);
		if(this.JackIn==null)
			setJackIn((byte) 0x6a);
		if(this.JackOut==null)
			setJackOut((byte) 0x6b);
		super.loadCodeTable(CustomisedCodeTable.codeTable, 20);
	}
	
	public void setVolume(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setVolume needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setVolume has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[0] = codes[1];
				CustomisedCodeTable.codeTable[1] = codes[0];
				this.Volume = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[1] = codes[0];
				this.Volume = new byte[] {codes[0]};
			}
		}
	}
	
	public void setLeft(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setLeft needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setLeft has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[2] = codes[1];
				CustomisedCodeTable.codeTable[3] = codes[0];
				this.Left = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[3] = codes[0];
				this.Left = new byte[] {codes[0]};
			}
		}
	}
	
	
	public void setRight(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setRight needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setRight has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[4] = codes[1];
				CustomisedCodeTable.codeTable[5] = codes[0];
				this.Right = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[5] = codes[0];
				this.Right = new byte[] {codes[0]};
			}
		}
	}
	
	public void setDown(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setDown needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setDown has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[6] = codes[1];
				CustomisedCodeTable.codeTable[7] = codes[0];
				this.Down = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[7] = codes[0];
				this.Down = new byte[] {codes[0]};
			}
		}
	}
	
	public void setUp(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setUp needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setUp has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[8] = codes[1];
				CustomisedCodeTable.codeTable[9] = codes[0];
				this.Up = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[9] = codes[0];
				this.Up = new byte[] {codes[0]};
			}
		}
	}
	
	public void setEnter(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setEnter needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setEnter has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[10] = codes[1];
				CustomisedCodeTable.codeTable[11] = codes[0];
				this.Enter = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[11] = codes[0];
				this.Enter = new byte[] {codes[0]};
			}
		}
	}
	
	public void setHorizontal(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setHorizontal needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setHorizontal has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[12] = codes[1];
				CustomisedCodeTable.codeTable[13] = codes[0];
				this.Horizontal = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[13] = codes[0];
				this.Horizontal = new byte[] {codes[0]};
			}
		}
	}
	
	public void setVertical(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setVertical needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setVertical has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[14] = codes[1];
				CustomisedCodeTable.codeTable[15] = codes[0];
				this.Vertical = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[15] = codes[0];
				this.Vertical = new byte[] {codes[0]};
			}
		}
	}
	
	public void setJackIn(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setJackIn needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setJackIn has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[16] = codes[1];
				CustomisedCodeTable.codeTable[17] = codes[0];
				this.JackIn = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[17] = codes[0];
				this.JackIn = new byte[] {codes[0]};
			}
		}
	}
	
	public void setJackOut(byte...codes) throws StormInterfaceException
	{
		if(codes.length<=0)
			throw new StormInterfaceException("setJackOut needs at least one argument");
		else if(codes.length>2)
			throw new StormInterfaceException("setJackOut has too many argument");
		else
		{
			try 
			{
				CustomisedCodeTable.codeTable[18] = codes[1];
				CustomisedCodeTable.codeTable[19] = codes[0];
				this.JackOut = new byte[] {codes[1],codes[0]};
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				CustomisedCodeTable.codeTable[19] = codes[0];
				this.JackOut = new byte[] {codes[0]};
			}
		}
	}
	
	public byte[] getCodeTableCode()
	{
		return CustomisedCodeTable.codeTable;
	}
	
	public byte[] getVolumeCode()
	{
		return this.Volume;
	}
	
	public byte[] getLeftCode()
	{
		return this.Left;
	}
	
	public byte[] getRightCode()
	{
		return this.Right;
	}
	
	public byte[] getDownCode()
	{
		return this.Down;
	}
	
	public byte[] getUpCode()
	{
		return this.Up;
	}
	
	public byte[] getEnterCode()
	{
		return this.Enter;
	}
	
	public byte[] getHorizontalCode()
	{
		return this.Horizontal;
	}
	
	public byte[] getVerticalCode()
	{
		return this.Vertical;
	}
	
	public byte[] getJackInCode()
	{
		return this.JackIn;
	}
	
	public byte[] getJackOutCode()
	{
		return this.JackOut;
	}
}
