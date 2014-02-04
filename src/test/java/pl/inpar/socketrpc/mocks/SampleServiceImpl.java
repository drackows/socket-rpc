package pl.inpar.socketrpc.mocks;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleServiceImpl implements SampleService {
	Logger logger = LoggerFactory.getLogger(SampleServiceImpl.class);
	
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

	@Override
    public void intAsArg(int i) {
	    logger.info("call with arg value: "+i);
    }

	@Override
    public String stringify(List<Integer> ints) {
	    return ints.toString();
    }
	
	
	
}
