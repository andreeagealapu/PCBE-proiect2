package pubsub;

import java.util.ArrayList;

public abstract class Actors 
{

    private String name;
    protected PubSubService service;

    public Actors(String name, PubSubService service) 
    {
        this.name = name;
        this.service = service;
    }

    public String getName() 
    {
        return name;
    }

    public abstract void callBack(Events e);

}