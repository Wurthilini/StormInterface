package StormInterfaceApi.utilities;

import java.util.Arrays;

public class VersionDecoder {

		private int start;
		private int end;
		
		public VersionDecoder()
		{
			this.start = -1;
			this.end = -1;
		}
		
		public int getStartPosition()
		{
			return this.start;
		}
		
		public int getEndPosition()
		{
			return this.end;
		}
		
		public void findVersionString(byte[] messageData, int Vstart)
		{
			if(Vstart!=-1)
			{
				int counter = 0;
				this.start = Vstart;
				for(byte bytes : messageData)
				{
					if(bytes == 0x2e)
					{
						this.end = counter+1;
						break;
					}
					counter++;
				}
			}
			else
			{
				for(byte bytes : messageData)
				{
					int counter = 0;
					if(bytes == 0x56)
						findVersionString(Arrays.copyOfRange(messageData, counter, messageData.length), counter);
					counter++;
				}
			}
		}
}
