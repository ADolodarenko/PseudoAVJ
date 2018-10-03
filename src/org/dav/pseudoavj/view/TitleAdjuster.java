package org.dav.pseudoavj.view;

import java.awt.*;

public interface TitleAdjuster
{
    void setComponentTitle(Component component, String titleKey);
    void changeComponentTitle(Component component, String titleKey);
    void registerComponent(Component component, String titleKey);
    void resetComponents();
}
