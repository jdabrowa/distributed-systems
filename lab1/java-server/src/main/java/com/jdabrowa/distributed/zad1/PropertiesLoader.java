package com.jdabrowa.distributed.zad1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private boolean initialized = false;
    private Properties properties;

    public void loadProperties(String fileName) throws IOException {
        InputStream propertyFileStream = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
        validateNotNull(fileName, propertyFileStream);
        loadProperties(propertyFileStream);
        this.initialized = true;
    }

    private void loadProperties(InputStream propertyFileStream) throws IOException {
        Properties properties = new Properties();
        properties.load(propertyFileStream);
        this.properties = properties;
    }

    private void validateNotNull(String fileName, InputStream propertyFileStream) throws FileNotFoundException {
        if(propertyFileStream == null) {
            throw new FileNotFoundException(String.format("File '%s' was not found", fileName));
        }
    }

    public String getProperty(String propertyKey) {
        if(!this.initialized) {
            throw new IllegalStateException("Properties have not been loaded yet, call loadProperties()");
        }
        return this.properties.getProperty(propertyKey);
    }
}
