package propertiesConfig;

import javax.xml.bind.annotation.XmlAttribute;

public class SerialNumberConfig {

	@XmlAttribute
	private String id;
	
	public String getSerialNumberID()
	{
		return this.id;
	}
}
