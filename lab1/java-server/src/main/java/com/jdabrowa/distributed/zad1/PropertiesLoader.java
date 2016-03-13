package com.jdabrowa.distributed.zad1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private final String fileName;
    private boolean initialized = false;
    private Properties properties;

    public PropertiesLoader(String fileName) {
        this.fileName = fileName;
    }

    public void loadProperties() throws IOException {
        InputStream propertyFileStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(propertyFileStream);
        this.properties = properties;
        this.initialized = true;
    }

    public String getProperty(String propertyKey) {
        if(!this.initialized) {
            throw new IllegalStateException("Properties have not been loaded yet, call loadProperties()");
        }
        return this.properties.getProperty(propertyKey);
    }
}
