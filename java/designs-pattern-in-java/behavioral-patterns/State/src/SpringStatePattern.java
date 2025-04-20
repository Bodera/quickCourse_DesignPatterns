import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SpringStatePattern 
{
    public static StateMachine<States, Events> buildMachine() 
        throws Exception
    {
        StateMachineBuilder.Builder<States, Events> builder 
            = StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
                .initial(States.OFF_HOOK)
                .states(EnumSet.allOf(States.class));

        builder.configureTransitions()
            .withExternal()
                .source(States.OFF_HOOK).target(States.CONNECTING)
                .event(Events.DIAL)
                .and()
            .withExternal()
                .source(States.OFF_HOOK).target(States.ON_HOOK)
                .event(Events.FINISH)
                .and()
            .withExternal()
                .source(States.CONNECTING).target(States.OFF_HOOK)
                .event(Events.HANG_UP)
                .and()
            .withExternal()
                .source(States.CONNECTING).target(States.CONNECTED)
                .event(Events.ANSWER)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.OFF_HOOK)
                .event(Events.LEFT_MSG)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.OFF_HOOK)
                .event(Events.HANG_UP)
                .and()
            .withExternal()
                .source(States.CONNECTED).target(States.ON_HOLD)
                .event(Events.ON_HOLD)
                .and()
            .withExternal()
                .source(States.ON_HOLD).target(States.CONNECTED)
                .event(Events.OFF_HOLD)
                .and()
            .withExternal()
                .source(States.ON_HOLD).target(States.OFF_HOOK)
                .event(Events.HANG_UP);

        return builder.build();
    }

    public static void main(String[] args) throws Exception
    {
        // create the state machine
        StateMachine<States, Events> machine = buildMachine();

        // start the state machine
        machine.startReactively().block();

        // define the exit state
        States exitState = States.ON_HOOK;

        // get the reader's input
        BufferedReader consoleReader = new BufferedReader(
            new InputStreamReader(System.in)
        );

        AtomicBoolean running = new AtomicBoolean(true);
        while (running.get())
        {
            State<States, Events> currentState = machine.getState();
            System.out.println("The phone is currently in " + currentState.getId() + " state.");
            System.out.println("What do you want to do?");

            List<Transition<States, Events>> transitions = 
                machine.getTransitions()
                    .stream()
                    .filter(t -> t.getSource().equals(currentState))
                    .collect(Collectors.toList());

            for (int i = 0; i < transitions.size(); i++)
            {
                System.out.println("" + i + ": " + transitions.get(i).getTrigger().getEvent());
            }

            boolean parseOk;
            int choice = 0;
            do
            {
                try 
                {
                    choice = Integer.parseInt(consoleReader.readLine());
                    parseOk = choice >= 0 && choice < transitions.size();
                } catch (IOException e) 
                {
                    parseOk = false;
                }
            } while (!parseOk);

            // make the transition
            Flux<StateMachineEventResult<States, Events>> resultFlux = 
            machine.sendEvent(
                Mono.just(
                    MessageBuilder.withPayload(
                        transitions.get(choice).getTrigger().getEvent())
                        .build()
                    ));

            resultFlux
            .doOnComplete(() -> {
                // check if we're in the exit state
                if (machine.getState().getId().equals(exitState)) {
                    // state machine has finished
                    System.out.println("The phone is now in " + machine.getState().getId() + " state.");

                    // stop the state machine
                    machine.stopReactively();

                    // stop the application
                    running.set(false);
                }
            })
            .subscribe();
        }
    }
}

enum States
{
    OFF_HOOK,
    ON_HOOK,
    CONNECTING,
    CONNECTED,
    ON_HOLD
}

enum Events
{
    DIAL,
    ANSWER,
    HANG_UP,
    ON_HOLD,
    OFF_HOLD,
    LEFT_MSG,
    FINISH
}
