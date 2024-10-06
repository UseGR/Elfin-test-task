package ru.scoring.camunda_app.service.filter;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;

public interface ScoringValidator {
    void validate(ScoringStartRequest request, DelegateExecution execution);

    void updateContext(DelegateExecution execution);
}
