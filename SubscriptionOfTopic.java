package pubsub;

public class SubscriptionOfTopic 
{

    private String domain;
    private Actors actor;

    //abonarea doar in functie de actor si domain si nu conteaza evenimentul

    public SubscriptionOfTopic(Actors actor, String domain)
    {
        this.actor = actor;
        this.domain = domain;
    }

    public Actors getActor() 
    {
        return actor;
    }
}
