package org.dav.pseudoavj.util;

import javax.swing.*;
import java.io.File;

public class ResourceManager
{
    private static ResourceManager instance;

    public static ResourceManager getInstance()
    {
        if (instance == null)
            instance = new ResourceManager();

        return instance;
    }

    private String appPath;

    private ResourceManager()
    {
        appPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    public ImageIcon getImageIcon(String name)
    {
        String fullPath = appPath + "images/" + name;
        File file = new File(fullPath);

        if (file.exists())
            return new ImageIcon(file.getAbsolutePath());
        else
            return null;
    }
}
