package org.dav.pseudoavj.model;

import org.dav.pseudoavj.util.AttrsKeeper;

public interface Attrs
{
	void load(AttrsKeeper keeper);
	void save(AttrsKeeper keeper);
}
