package pl.inpar.socketrpc.mocks;

import java.util.List;

public interface SampleService {
	
	void doStuff();
	int getValue();
	
	void intAsArg(int i);
	
	String stringify(List<Integer> ints);
}
