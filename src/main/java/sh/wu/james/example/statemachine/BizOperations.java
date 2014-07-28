package sh.wu.james.example.statemachine;

public interface BizOperations {
    BizOperations wakeUp();

    BizOperations eatBreakfast();

    BizOperations eatLunch();

    BizOperations sleep();
}
