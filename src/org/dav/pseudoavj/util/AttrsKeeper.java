package org.dav.pseudoavj.util;

import org.dav.pseudoavj.model.FileAttrs;
import org.dav.pseudoavj.model.WindowAttrs;

public interface AttrsKeeper
{
	boolean load(FileAttrs attrs);
	boolean load(WindowAttrs attrs);
	boolean save(FileAttrs attrs);
	boolean save(WindowAttrs attrs);
}
