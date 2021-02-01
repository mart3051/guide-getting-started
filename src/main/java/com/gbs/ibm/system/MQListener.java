package com.gbs.ibm.system;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

//import com.google.gson.Gson;

@MessageDriven(name = "SampleListenerMDB")
public class MQListener implements MessageListener {
    @Inject
    EventDAO eventDAO;
	public void onMessage(Message message) {
		try {
            /*String messageString=message.getBody(String.class);
            System.out.println(messageString);
            Gson g = new Gson();  */
            EvenWrapper event = message.getBody(EvenWrapper.class);
            Event newEvent = new Event(event.getName(),event.getLocation(),event.getTime());
            eventDAO.createEvent(newEvent);
			System.out.println("MDB received: " + event.toString());
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
    }
}
