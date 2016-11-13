package com.example.guestbook;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;


/**
 * Resource which has only one representation.
 *
 */
public class HelloWorldResource extends ServerResource {

    @Get(value = "xml")
    public String represent() {
        List<Greeting> allGreetings = ObjectifyService.ofy()
            .load()
            .type(Greeting.class) // We want only Greetings
            .order("-date")       // Most recent first - date is indexed.
            .list();

        String guestbook = new String("<guestbook>");
        int length = allGreetings.size();
        Greeting greeting = new Greeting();
        for (int i = 0; i < length; i++) {
            greeting = allGreetings.get(i);
            guestbook = guestbook+"<greeting><theBook>default</theBook>";
            guestbook = guestbook+"<id>"+greeting.id+"</id>";
            guestbook = guestbook+"<author_email>"+greeting.author_email+"</author_email>";
            guestbook = guestbook+"<author_id>"+greeting.author_id+"</author_id>";
            guestbook = guestbook+"<content>"+greeting.content+"</content>";
            guestbook = guestbook+"<date>"+greeting.date+"</date>";
            guestbook = guestbook+"</greeting>";
        }
        guestbook += "</guestbook>";
        return guestbook;
    }

}