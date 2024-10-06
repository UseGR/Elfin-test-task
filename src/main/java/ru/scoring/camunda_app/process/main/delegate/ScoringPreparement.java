package ru.scoring.camunda_app.process.main.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.delegate.DelegateBase;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.service.filter.ScoringValidator;

import java.util.List;

import static ru.scoring.camunda_app.controller.EventVariables.START_SCORING;

@Component("ScoringPreparement")
@RequiredArgsConstructor
public class ScoringPreparement extends DelegateBase {
    private final List<ScoringValidator> filters;

    @Override
    public void doExecute(DelegateExecution execution) {
        ScoringStartRequest scoringStartRequest = Classifier.getVariable(execution, START_SCORING);

        filters.forEach(filter -> filter.validate(scoringStartRequest, execution));
    }
}
