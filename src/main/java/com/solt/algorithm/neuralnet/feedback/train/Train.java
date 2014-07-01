package com.solt.algorithm.neuralnet.feedback.train;

import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;

public interface Train {

	void iteration();

	double getError();
	
	FeedforwardNetwork getNetwork();

}
