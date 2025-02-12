import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class ReactxEventBroker {
    public static void main(String[] args) 
    {
        EventBroker broker = new EventBroker();
        FootballPlayer player = new FootballPlayer(broker, "Antony");
        FootballCoach coach = new FootballCoach(broker, player);

        player.score();
        player.score();
    }
}

class EventBroker extends Observable<FootballPlayer>
{

    private List<Observer<? super FootballPlayer>> observers = new ArrayList<>();

    @Override
    protected void subscribeActual(Observer<? super FootballPlayer> observer)
    {
        observers.add(observer);
    }

    public void publish(FootballPlayer player) 
    {
        for (Observer<? super FootballPlayer> observer : observers) {
            observer.onNext(player);
        }
    }
}

class FootballPlayer 
{
    public int goalsScored = 0;
    private EventBroker broker;
    public String name;

    public FootballPlayer(EventBroker broker, String name) 
    {
        this.broker = broker;
        this.name = name;
    }

    public void score() 
    {
        ++goalsScored;
        broker.publish(this);
    }
}

class FootballCoach 
{
    public FootballCoach(EventBroker broker, FootballPlayer player) 
    {
        broker.subscribe(goals -> {
            System.out.println("The coach congratulates " + player.name + " for scoring " + player.goalsScored + " goals!");
        });
    }
}
