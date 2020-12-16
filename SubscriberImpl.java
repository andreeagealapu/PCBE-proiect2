package pubsub;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SubscriberImpl extends Actors implements Runnable
{
    public List<Message> subscriberMessages = new ArrayList<Message>(); //lista cu mesajele subcriberilor
    private String name;

    public SubscriberImpl(String name, PubSubService service)
    {
        super(name, service);
    }

    //adaugarea  subscriberilor cu PubSubService pentru un domaniu
    public void registerListener(String eventType, String domain)
    {
        service.addSubscriber(eventType, this, domain);
        System.out.println("Cititorul "+ this.getName() + " s-a abonat pentru evenimente de tipul " + eventType);

    }

    public void callBack(Events e)
    {
        Events.EventType eventType = e.getType();

        Lock _mutex = new ReentrantLock(true);

        _mutex.lock();

        switch (eventType)
        {
            case Added : {
                Events ReadNewsEvent = new Events(Events.EventType.Added, e.getMessage());
                service.sendEvent(ReadNewsEvent); //trimit un eveniment ca stirea s-a citit
                subscriberMessages.add(e.getMessage());
                System.out.println("S-a adaugat stirea de tipul " + e.getEventType() + " din domeniul " + e.getMessage().getDomain());

                //setez data publicarii stirii
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                e.getMessage().setPublicationDate(date);

                System.out.println("Data publicarii acesteia este " + date);

                break;
            }


            case Deleted : {
                int ok = 0;
                try {
                    for (Message mess : subscriberMessages)
                    {
                        if (mess.getDomain().equals(e.getMessage().getDomain()) &&
                                mess.getPayload().equals(e.getMessage().getPayload()))
                        {
                            System.out.println("Mesajul cu domeniul " + mess.getDomain() + " si textul ''" + mess.getPayload() + "'' a fost sters.\n");
                            subscriberMessages.remove(e.getMessage());
                            ok = 1;
                        } else {
                            System.out.println("Mesajul pe care doriti sa il stergeti nu exista.");
                            ok = 1;
                        }
                    }
                } catch (ConcurrentModificationException e1) {
                    System.out.println(e1);
                }
                if (ok == 0)
                    System.out.println("Nu exista stiri pentru a fi sterse!");

                break;
            }

            case Modified : {
                try {
                    for (Message mess : subscriberMessages)
                    {
                        if (mess.getDomain().equals(e.getMessage().getDomain()) &&
                                mess.getPayload().equals(e.getMessage().getPayload()))  //daca asta e stirea pe care vreau sa  modific
                        {
                            Message currentMessage = e.getMessage();    //pun in variabila stirea pe care vreau sa o modific
                            System.out.println("Stirea cu domeniul " + currentMessage.getDomain() + " si textul ''" + currentMessage.getPayload() + "'' este in curs de modificare.\n.\n.");

                            Thread.sleep(2000);

                            String newText = e.getNewText();
                            currentMessage.modifyText(newText);

                            //setez data ultimei modificari
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                            Date date = new Date(System.currentTimeMillis());
                            mess.setModifiedDate(date);

                            System.out.println("Domeniul noului mesaj este: " + currentMessage.getDomain() + ", iar textul este ''" + currentMessage.getPayload() + "''");

                            System.out.println("Data modificarii acestuia este " + date);

                        }
                    }
                } catch (Exception exception) {
                    System.out.println(exception);
                }
                break;

            }
        }
        _mutex.unlock();
    }

    @Override
    public void run()
    {
        PubSubService service = new PubSubService();

        PublisherImpl pub1 = new PublisherImpl("Ana", service);
        PublisherImpl pub2= new PublisherImpl("George", service);

        Message m1 = new Message("Sports","Football","DigiSport", pub1.getName(),"Meci Steaua-Dinamo 2-2");
        Message m2 = new Message("Arts","Painting","Luvru", pub2.getName(),"Secrets about Monalisa");

        Events e1 = new Events(Events.EventType.Added,m1);
        Events e2 = new Events(Events.EventType.Modified,m1,"Meci Rapid-Steaua 1-1","Meci Steaua-Dinamo 2-2");
        Events e3 = new Events(Events.EventType.Deleted,m1);

        this.registerListener("Added","Sports");
        this.registerListener("Modified","Sports");
        this.registerListener("Deleted","Sports");

        Events e4 = new Events(Events.EventType.Added,m2);
        Events e5 = new Events(Events.EventType.Modified,m2,"Biografy of Da Vinci","Secrets about Monalisa");
        Events e6 = new Events(Events.EventType.Deleted,m2);

        this.registerListener("Added","Arts");
        this.registerListener("Modified","Arts");
        this.registerListener("Deleted","Arts");

        callBack(e1);
        callBack(e2);
        callBack(e3);

        callBack(e4);
        callBack(e5);
        callBack(e6);
    }
}
