package ru.scoring.camunda_app.process.main.delegate;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.context_api_controller._1_0_0.client.api.ContextV10ApiClient;
import ru.scoring.camunda_app.engine.delegate.DelegateBase;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.mapper.ContextApiMapper;
import ru.scoring.camunda_app.process.main.variable.Context;

import java.util.UUID;

import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.*;

@Component("ScoringEnd")
@RequiredArgsConstructor
public class ScoringEnd extends DelegateBase {
    private final ContextV10ApiClient contextApiClient;
    private final ContextApiMapper contextApiMapper;

    @Override
    public void doExecute(DelegateExecution execution)  {
        Context context = Classifier.getVariable(execution, CONTEXT);
        UUID processId = Classifier.getVariable(execution, PROCESS_ID);
        Boolean isScoringFailed = Classifier.getVariable(execution, IS_SCORING_FAILED);

        contextApiClient.contextSave(contextApiMapper.mapContext(context, processId, isScoringFailed));
    }
}
