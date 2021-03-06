package org.dav.pseudoavj;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager
{
    private static ResourceManager instance;
    
    public static final Locale RUS_LOCALE = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
    public static final Locale ENG_LOCALE = new Locale.Builder().setLanguage("en").setRegion("US").build();

    public static ResourceManager getInstance()
    {
        if (instance == null)
            instance = new ResourceManager();

        return instance;
    }

    private Locale currentLocale;
    private ResourceBundle bundle;

    private ResourceManager()
    {
        setCurrentLocale(ENG_LOCALE);
    }

    public Locale getCurrentLocale()
    {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale)
    {
        this.currentLocale = currentLocale;
        bundle = ResourceBundle.getBundle("org.dav.pseudoavj.langs.pseudoavj", this.currentLocale);
    }

    public void switchCurrentLocale()
    {
        if (getCurrentLocale() == ENG_LOCALE)
            setCurrentLocale(RUS_LOCALE);
        else
            setCurrentLocale(ENG_LOCALE);
    }

    public ResourceBundle getBundle()
    {
        return bundle;
    }

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
