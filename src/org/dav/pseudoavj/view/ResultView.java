package org.dav.pseudoavj.view;

import java.util.List;

public interface ResultView<P, R, M>
{
	void updateData(List<P> chunks);
	
	void showResult(R data, M statistics);

	void activateControls();

	void blockControls();
}
