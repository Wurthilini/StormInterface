package propertiesConfig;

import javax.xml.bind.annotation.XmlAttribute;

public class KeypadTableConfig {

	@XmlAttribute
	private int id;
	
	public int getKeypadTableID()
	{
		return this.id;
	}
}
