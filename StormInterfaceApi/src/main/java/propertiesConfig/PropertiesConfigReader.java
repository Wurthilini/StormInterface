package propertiesConfig;

import java.io.FileReader;

import javax.xml.bind.JAXB;

public class PropertiesConfigReader {

	    public PropertiesConfig readPropertiesConfig()
	        throws Exception
	    {
	        try (FileReader fileReader = new FileReader("../StormInterfaceApi/config/PropertiesConfig.xml"))
	        {
	            return JAXB.unmarshal(fileReader, PropertiesConfig.class);
	        }
	    }
}
