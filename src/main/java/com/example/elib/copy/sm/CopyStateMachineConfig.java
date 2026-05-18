package com.example.elib.copy.sm;

import com.example.elib.copy.enums.CopyEvent;
import com.example.elib.copy.enums.CopyStatus;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CopyStateMachineConfig {

    public StateMachine<CopyStatus, CopyEvent> createMachine(CopyStatus initialState) {
        return new StateMachine<>(initialState, config);
    }

    private final StateMachineConfig<CopyStatus, CopyEvent> config = buildConfig();

    private StateMachineConfig<CopyStatus, CopyEvent> buildConfig() {
        StateMachineConfig<CopyStatus, CopyEvent> config = new StateMachineConfig<>();

        config.configure(CopyStatus.ADDED)
                .permit(CopyEvent.MAKE_AVAILABLE, CopyStatus.AVAILABLE);

        config.configure(CopyStatus.AVAILABLE)
                .permit(CopyEvent.RESERVE, CopyStatus.RESERVED)
                .permit(CopyEvent.ISSUE, CopyStatus.ISSUED)
                .permit(CopyEvent.WRITE_OFF, CopyStatus.WRITTEN_OFF);

        config.configure(CopyStatus.RESERVED)
                .permit(CopyEvent.ISSUE, CopyStatus.ISSUED)
                .permit(CopyEvent.CANCEL_RESERVE, CopyStatus.AVAILABLE);

        config.configure(CopyStatus.ISSUED)
                .permit(CopyEvent.RETURN, CopyStatus.IN_TRANSIT);

        config.configure(CopyStatus.IN_TRANSIT)
                .permit(CopyEvent.SHELVE, CopyStatus.AVAILABLE);

        return config;
    }
}
