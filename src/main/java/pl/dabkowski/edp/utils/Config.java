package pl.dabkowski.edp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config instance;
    private final String fileName = "config.properties";
    private Properties properties;
    private InputStream inputStream;

    private Config() {
        loadProperties(fileName);
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private void loadProperties(String fileName) {
        properties = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            System.out.println("Unable to open properties file " + fileName);

            return;
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getProperty(String prop) {
        return properties.getProperty(prop);
    }



}
