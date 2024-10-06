package ru.scoring.camunda_app.process.main.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.delegate.DelegateBase;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.variable.Context;

import java.util.UUID;

import static ru.scoring.camunda_app.controller.EventVariables.START_SCORING;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.*;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.SCORING_SUCCESS;

@Component("InitializeVariables")
public class InitializeVariables extends DelegateBase {
    @Override
    public void doExecute(DelegateExecution execution) {
        ScoringStartRequest scoringStartRequest = Classifier.getVariable(execution, START_SCORING);

        Classifier.setVariable(execution, CONTEXT, buildContext(scoringStartRequest));
        Classifier.setVariable(execution, PROCESS_ID, UUID.fromString(execution.getProcessInstanceId()));
        Classifier.setVariable(execution, IS_INDIVIDUAL_UNDERTAKER, null);
        Classifier.setVariable(execution, IS_COMPANY_IN_PROHIBITED_REGION, null);
        Classifier.setVariable(execution, IS_CAPITAL_LOW, null);
        Classifier.setVariable(execution, IS_RESIDENT_COMPANY, null);
        Classifier.setVariable(execution, IS_SCORING_FAILED, null);
    }

    private Context buildContext(ScoringStartRequest scoringStartRequest) {
        Context context = Context.builder()
                .individualTaxpayerNumber(scoringStartRequest.getIndividualTaxpayerNumber())
                .regionalCode(scoringStartRequest.getRegionalCode())
                .capital(scoringStartRequest.getCapital())
                .build();

        context.getScoringResult().add(SCORING_SUCCESS);

        return context;
    }
}
