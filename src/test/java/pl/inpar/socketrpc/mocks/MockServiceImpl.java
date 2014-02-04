package pl.inpar.socketrpc.mocks;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockServiceImpl implements MockService {
	Logger logger = LoggerFactory.getLogger(MockServiceImpl.class);
	
	AtomicInteger value=new AtomicInteger();
	
	@Override
	public void doStuff() {
		value.incrementAndGet();
		logger.info("some stuff ==:-) ");
	}

	@Override
    public int getValue() {
		logger.info("getting value: "+value);
		return value.intValue();
    }
	
	
	
}
