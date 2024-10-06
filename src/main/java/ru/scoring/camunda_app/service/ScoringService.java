package ru.scoring.camunda_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartResponse;

import java.util.Map;
import java.util.UUID;

import static ru.scoring.camunda_app.controller.EventVariables.START_SCORING;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {
    private final RuntimeService runtimeService;
    public static final String MAIN_PROCESS_KEY = "main_process";

    public ScoringStartResponse startScoring(ScoringStartRequest request) {
        Map<String, Object> variables = Map.ofEntries(
                Map.entry(START_SCORING.key(), request)
        );

        ProcessInstance processInstance;
        try {
            processInstance = runtimeService.startProcessInstanceByKey(MAIN_PROCESS_KEY, variables);
        } catch (ProcessEngineException e) {
            log.error("Ошибка запуска процесса с запросом: {}", request);
            throw new RuntimeException(e);
        }

        return ScoringStartResponse.builder()
                .id(UUID.fromString(processInstance.getProcessInstanceId()))
                .build();
    }
}
