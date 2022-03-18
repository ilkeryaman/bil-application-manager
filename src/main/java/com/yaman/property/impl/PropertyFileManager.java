package com.yaman.property.impl;

import com.yaman.property.IPropertyManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public abstract class PropertyFileManager implements IPropertyManager {

    protected String propertyName;
    private Properties properties;

    @Override
    public void loadProperties() {
        Properties props = new Properties();
        try {
            props.load(new FileReader(new File(".").getCanonicalPath() + File.separator + propertyName));
            setProperties(props);
        } catch (IOException exc) {
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
