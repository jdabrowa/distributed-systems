package com.jdabrowa.distributed.zad2;

import com.jdabrowa.distributed.zad1.PropertiesLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    private static final String IMAGE_DIR = "img";
    private static final String MAPPING_FILE_NAME = "imageNamesMapping.properties";
    private final PropertiesLoader loader;

    public ImageLoader(PropertiesLoader loader) throws IOException {
        this.loader = loader;
        this.loader.loadProperties(MAPPING_FILE_NAME);
    }

    public InputStream loadImageAsStream(String imageName) throws FileNotFoundException {

        String imagePath = pathForImage(imageName);
        InputStream inputStream = inputStreamForPath(imagePath);
        validateStreamNotNull(imageName, inputStream, imagePath);
        return inputStream;
    }

    private InputStream inputStreamForPath(String imagePath) {
        return ImageLoader.class.getClassLoader().getResourceAsStream(imagePath);
    }

    private String pathForImage(String imageName) throws FileNotFoundException {
        return IMAGE_DIR + File.separator + getFileNameForImage(imageName);
    }

    private void validateStreamNotNull(String imageName, InputStream inputStream, String path) throws FileNotFoundException {
        if(inputStream == null) {
            String message = String.format("Image '%s' with path %s does not exist", imageName, path);
            throw new FileNotFoundException(message);
        }
    }

    public String getFileNameForImage(String imageName) throws FileNotFoundException {
        String fileName = loader.getProperty(imageName);
        if(fileName == null) {
            throw new FileNotFoundException("No file is available under name '" + imageName + "'");
        }
        return fileName;
    }

    public boolean isMappingAvailableFor(String imageName) {
        return loader.getProperty(imageName) != null;
    }
}
