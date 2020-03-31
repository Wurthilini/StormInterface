package propertiesConfig;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CustomisedTableCode")
public class CustomisedTableCodeConfig {
	
	@XmlAttribute
	private String id;
	
	@XmlAttribute
	private String usbUsageName;
	
	@XmlAttribute
	private String modifierName;
	
	public String getID()
	{
		return this.id;
	}
	
	public String getUsbUsageName()
	{
		return this.usbUsageName;
	}
	
	public String getModifierName()
	{
		return this.modifierName;
	}
	
}
