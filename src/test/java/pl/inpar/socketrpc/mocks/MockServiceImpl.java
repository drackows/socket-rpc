package pl.inpar.socketrpc.mocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockServiceImpl implements MockService {
	Logger logger = LoggerFactory.getLogger(MockServiceImpl.class);
	
	Integer value=0;
	
	@Override
	public void doStuff() {
		synchronized (MockServiceImpl.class) {
			value++;
			logger.info("some stuff ==:-) ");
        }
	}

	@Override
    public int getValue() {
		logger.info("getting value: "+value);
		return value;
    }
	
	
	
}
