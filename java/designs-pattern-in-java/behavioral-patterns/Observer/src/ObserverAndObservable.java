import java.util.ArrayList;
import java.util.List;

public class ObserverAndObservable implements Observer<Person> {
    public static void main(String[] args) {
        new ObserverAndObservable();
    }

    public ObserverAndObservable() {
        Person person = new Person();
        person.subscribe(this);
        
        for (int i = 1; i < 5; i++) {
            person.setAge(i);
        }
    }

    @Override
    public void handle(PropertyChangedEventArgs<Person> args) {
        System.out.println("Person's " + args.getPropertyName() 
        + " changed to " + args.getNewValue());
    }
}

class Person extends Observable<Person> {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age == this.age) return;
        this.age = age;
        super.propertyChanged(this, "age", age);
    }
}

class PropertyChangedEventArgs<T> {
    private T source;
    private String propertyName;
    private Object newValue;

    public PropertyChangedEventArgs(T source, 
                                    String propertyName, 
                                    Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }
                                    
    public T getSource() {
        return source;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public Object getNewValue() {
        return newValue;
    }
}

interface Observer<T> {
    void handle(PropertyChangedEventArgs<T> args);
}

class Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    protected void propertyChanged(T source, 
                                    String propertyName, 
                                    Object newValue) {
        for (Observer<T> observer : observers) {
            observer.handle(new PropertyChangedEventArgs<T>(
                source, propertyName, newValue));
        }
    }
}

