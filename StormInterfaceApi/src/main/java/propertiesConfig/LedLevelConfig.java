package propertiesConfig;

import javax.xml.bind.annotation.XmlAttribute;

public class LedLevelConfig {

	@XmlAttribute
	private int level;
	
	public int getLedLevel()
	{
		return this.level;
	}
}
