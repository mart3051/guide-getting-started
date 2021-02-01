package com.gbs.ibm.system;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Stateless
public class MQProducer {

	@Inject
	@JMSConnectionFactory("jms/wmqCF")
	JMSContext context;

	@Resource(lookup = "jms/queue1")
	Queue queue;

	public void sendMessage(EvenWrapper event) throws Exception {
		context.createProducer().send(queue, event);
		System.out.println("Event enqueued.");
	}

}