package org.dav.pseudoavj.util;

import org.dav.pseudoavj.model.FileAttrs;

public interface AttrsKeeper
{
	boolean load(FileAttrs attrs);
	boolean save(FileAttrs attrs);
}
