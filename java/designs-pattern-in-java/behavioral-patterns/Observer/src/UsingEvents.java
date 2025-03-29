import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UsingEvents {
    public static void main(String[] args) {
        Person person = new Person();
        Event<PropertyChangedEventArgs>.Subscription 
        subscription = person.propertyChanged.addHandler(tArgs -> {
            System.out.println("Person's " + tArgs.propertyName + " changed to " + tArgs.source);
        });

        for (int i = 0; i < 2; i++) {
            person.setAge(i);
        }

        subscription.close();

        for (int i = 2; i < 4; i++) {
            person.setAge(i);
        }
    }
}

class Event<TArgs> 
{
    private int count = 0;
    private Map<Integer, Consumer<TArgs>> 
        handlers = new HashMap<>();

    public Subscription addHandler(Consumer<TArgs> handler) {
        int id = count++;
        handlers.put(id, handler);
        return new Subscription(this, id);
    }

    public void fire(TArgs args) 
    {
        for (Consumer<TArgs> handler : handlers.values()) {
            handler.accept(args);
        }
    }

    public class Subscription implements AutoCloseable 
    {
        private Event<TArgs> event;
        private int id;

        public Subscription(Event<TArgs> event, int id) 
        { 
            this.event = event;
            this.id = id;
        }

        @Override
        public void close() {
            event.handlers.remove(id);
         }
    }
}

class PropertyChangedEventArgs {
    public Object source;
    public String propertyName;

    public PropertyChangedEventArgs(Object source, String propertyName) {
        this.source = source;
        this.propertyName = propertyName;
    }
}

class Person 
{
    public Event<PropertyChangedEventArgs> propertyChanged;

    public Person() {
        propertyChanged = new Event<>();
    }

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age == this.age) return;
        this.age = age;

        propertyChanged.fire(new PropertyChangedEventArgs(age, "age"));
    }
}
