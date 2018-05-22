package org.dav.pseudoavj;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceManager
{
    private static ResourceManager instance;

    public static ResourceManager getInstance()
    {
        if (instance == null)
            instance = new ResourceManager();

        return instance;
    }

    private ResourceManager(){}
    
    public File getConfig()
    {
        File result = null;
        String fullJarPath = null;
        
        try
        {
            fullJarPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        }
        catch (URISyntaxException e){}
    
        if (fullJarPath != null)
        {
            String fullConfPath = Paths.get(fullJarPath).getParent().toAbsolutePath() + "/pav.conf";
            
            result = new File(fullConfPath);
        }
        
        return result;
    }

    public ImageIcon getImageIcon(String name)
    {
        URL imageURL = getClass().getResource("images/" + name);
        
        if (imageURL != null)
            return new ImageIcon(imageURL);
        else
            return null;
    }
}
