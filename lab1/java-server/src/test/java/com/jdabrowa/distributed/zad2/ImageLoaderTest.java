package com.jdabrowa.distributed.zad2;

import com.jdabrowa.distributed.zad1.PropertiesLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageLoaderTest {

    private ImageLoader instance;
    private PropertiesLoader loader;

    @Before
    public void setUp() throws IOException {
        this.loader = mock(PropertiesLoader.class);
        this.instance = new ImageLoader(loader);
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionOnNonExistingImage() throws FileNotFoundException {

        // given
        String nonExistingImageName = "non-existing-dadasasdas";

        // when
        instance.loadImageAsStream(nonExistingImageName);

        // then
        fail("Exception should have been thrown");
    }

    @Test
    public void shouldProperlyLoadExistingImage() throws FileNotFoundException {

        // given
        String existingImageName = "home";
        when(loader.getProperty("home")).thenReturn("home.jpg");

        // when
        InputStream inputStream = instance.loadImageAsStream(existingImageName);

        // then
        assertNotNull(inputStream);
    }

}