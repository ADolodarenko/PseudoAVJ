package org.dav.pseudoavj.view;

import java.awt.*;

public interface AdjustableTitles
{
	void registerComponent(String key, Component component);
	void resetComponents();
    String getComponentTitle(String key);
}
