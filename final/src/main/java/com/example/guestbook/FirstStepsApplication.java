package com.example.guestbook;

import java.util.List;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

import com.googlecode.objectify.ObjectifyService;

public class FirstStepsApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        Restlet greetingId = new Restlet() {
            @Override
            public void handle(Request request, Response response) {
                String message = new String();
                Long greetingId = Long.parseLong((String)(request.getAttributes().get("greetingId")));
                Greeting greeting = new Greeting();
                boolean hasGreeting = false;

                List<Greeting> allGreetings = ObjectifyService.ofy()
                    .load()
                    .type(Greeting.class) // We want only Greetings
                    .order("-date")       // Most recent first - date is indexed.
                    .list();
                int length = allGreetings.size();
                for (int i = 0; i < length; i++) {
                    if ((allGreetings.get(i).id).equals(greetingId)) {
                        greeting = allGreetings.get(i);
                        hasGreeting = true;
                        break;
                    }
                }

                if (hasGreeting) {
                    message = "<greeting><theBook>default</theBook>";
                    message = message+"<id>"+greeting.id+"</id>";
                    message = message+"<author_email>"+greeting.author_email+"</author_email>";
                    message = message+"<author_id>"+greeting.author_id+"</author_id>";
                    message = message+"<content>"+greeting.content+"</content>";
                    message = message+"<date>"+greeting.date+"</date>";
                    message = message+"</greeting>";
                    response.setEntity(message, MediaType.TEXT_XML);
                } else {
                    // Print the requested URI path
                    message = "Greeting with id "
                            + greetingId + " doesn't exist.";
                    response.setEntity(message, MediaType.TEXT_PLAIN);
                }
            }
        };

        router.attachDefault(HelloWorldResource.class);
        // Attach the handlers to the root router
        router.attach("/{greetingId}", greetingId);
        return router;
    }
}