import java.util.ArrayList;
import java.util.List;

class ChatRoomMediator
{
    public static void main(String[] args) 
    {
        ChatRoom room = new ChatRoom();
        Person alice = new Person("Alice");
        Person bob = new Person("Bob");
        Person charlie = new Person("Charlie");
    
        room.join(alice);
        room.join(bob);

        alice.say("Hello");
        bob.say("Hi there!");

        room.join(charlie);
        charlie.say("Hi everyone!");

        alice.privateMessage("Charlie", "Hi Charlie!");
    }
}

class ChatRoom 
{
    private List<Person> participants = new ArrayList<>();

    public void join(Person p) 
    {
        String joinMsg = p.name + " has joined the room.";
        broadcast("room", joinMsg);

        p.room = this;
        participants.add(p);
    }

    public void broadcast(String source, String message)
    {
        participants.stream()
            .filter(p -> !p.name.equals(source)) // validating that the person is not broadcasting to themself
            .forEach(p -> p.receive(source, message));
    }

    public void message(String from, String to, String message) 
    {
        participants.stream()
            .filter(p -> p.name.equals(to))
            .findFirst()
            .ifPresent(p -> p.receive(from, message));
    }
}

class Person 
{
    String name;
    ChatRoom room;
    List<String> chatLog = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public void receive(String sender, String message) 
    {
        String s = sender + ": '" + message + "'";
        System.out.println("[" + name + "'s chat session] " + s);
        chatLog.add(s);
    }

    public void say(String message) 
    {
        room.broadcast(name, message);
    }

    public void privateMessage(String who, String message) 
    {
        room.message(name, who, message);
    }
}