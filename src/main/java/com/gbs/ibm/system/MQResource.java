package com.gbs.ibm.system;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;


@Path("/enqueue")
@Stateless
public class MQResource {

	@Inject
	MQProducer producer;

	// Should be POST, using GET for the simplicity of the example
	@POST
	public Response enqueue(@RequestBody EvenWrapper event) throws Exception {
		producer.sendMessage(event);
		return Response.ok().entity("Event enqueued").build();
	}

}