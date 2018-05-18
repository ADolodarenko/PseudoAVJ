package org.dav.pseudoavj;

import java.util.List;

public interface ResultView<P, R, M>
{
	void updateData(List<P> chunks);
	
	void showResult(R data, M message);

	void activateControls();

	void blockControls();
}