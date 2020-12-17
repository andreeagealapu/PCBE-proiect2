package pubsub;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PublisherImpl extends Actors implements Runnable
{

    HashMap<String, Integer> counter = new HashMap<>();
    public PublisherImpl(String name, PubSubService service)
    {
        super(name, service);
    }

    public void registerListener(String eventType, String domain) 
    {

        service.addSubscriber(eventType, this, domain);
        System.out.println("Editorul "+ this.getName() + " s-a abonat pentru evenimente de tipul " + eventType + " pentru domeniul " + domain);

    }

    public void callBack(Events e)
    {
        Lock _mutex = new ReentrantLock(true);

        _mutex.lock();

        if(e.getEventType().equals("Read")) 
        {
            //daca nu am nimic in Map pentru counter, adica daca nu a mai fost citita nicio stire din domeniu, il pun pe 1, altfel - incrementez
            if(counter.get(e.getMessage().getDomain()) == null)
            {
                counter.put(e.getMessage().getDomain(), 1);
            }else {
                counter.put(e.getMessage().getDomain(), counter.get(e.getMessage().getDomain()) + 1);
            }

            System.out.println("Stirea "+ e.getMessage().getPayload() + " din domeniul " + e.getMessage().getDomain() + " a fost citita.");
            System.out.println("NUMARUL DE CITITORI pentru stirea din domeniul "+ e.getMessage().getDomain()  + " este: "+ counter.get(e.getMessage().getDomain()));
        }
        
        _mutex.unlock();
    }

    @Override
    public void run() {

        Message m1 = new Message("Sports","Footbal","DigiSport", this.getName(),"Meci Steaua-Dinamo 2-2");
        Message m2 = new Message("Arts","Painting","Luvru", this.getName(),"Secrets about Monalisa");

        Events e1 = new Events(Events.EventType.Read,m1);
        Events e2 = new Events(Events.EventType.Read,m1);
        Events e3 = new Events(Events.EventType.Read,m2);

        this.registerListener("Read","Sport");
        this.registerListener("Read","Sport");
        this.registerListener("Read","Arts");

        callBack(e1);
        callBack(e2);
        callBack(e3);

    }
}
