package propertiesConfig;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "propertiesConfig")
@XmlType(propOrder = { "ledLevelConfig", "keypadTableConfig", "serialNumberConfig", "customisedTableCodesConfigs"})

@XmlAccessorType(XmlAccessType.FIELD)
public class PropertiesConfig 
{
	
	
    @XmlElement(name = "ledLevel")
    private LedLevelConfig ledLevelConfig = new LedLevelConfig();
    
    @XmlElement(name = "keypadTable")
    private KeypadTableConfig keypadTableConfig = new KeypadTableConfig();
    
    @XmlElement(name = "serialNumber")
    private SerialNumberConfig serialNumberConfig = new SerialNumberConfig();
    
    @XmlElementWrapper(name = "customisedTableCodes")
    @XmlElement(name = "customisedTableCode")
    private List<CustomisedTableCodeConfig> customisedTableCodesConfigs = new ArrayList<CustomisedTableCodeConfig>();
    
    public LedLevelConfig getLedLevelConfig()
    {
    	return this.ledLevelConfig;
    }
    
    public KeypadTableConfig getKeypadTableConfig()
    {
    	return this.keypadTableConfig;
    }
    
    public SerialNumberConfig getSerialNumberConfig()
    {
    	return this.serialNumberConfig;
    }
    
    public List<CustomisedTableCodeConfig> getCustomisedTableCodeConfig()
    {
    	return this.customisedTableCodesConfigs;
    }
}
