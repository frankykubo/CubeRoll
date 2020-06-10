package sk.tuke.gamestudio.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public abstract class ServiceProperties {
    public static Properties getServiceProperties(){
        Properties props = new Properties();
        File configFile = new File("src/main/resources/"+"service.properties");
        FileReader reader = null;
        try {
            reader = new FileReader(configFile);
            props.load(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
