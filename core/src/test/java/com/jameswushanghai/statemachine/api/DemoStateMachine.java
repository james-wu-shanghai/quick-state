package com.jameswushanghai.statemachine.api;

import com.jameswushanghai.statemachine.core.StateMachine;

public interface DemoStateMachine extends StateMachine {
        DemoStateMachine start();
        DemoStateMachine retry();
    }