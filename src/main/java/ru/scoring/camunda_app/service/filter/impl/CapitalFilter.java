package ru.scoring.camunda_app.service.filter.impl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.variable.Context;
import ru.scoring.camunda_app.service.filter.ScoringValidator;

import java.util.Optional;

import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.CONTEXT;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.IS_CAPITAL_LOW;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.CAPITAL_FAILURE;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.SCORING_SUCCESS;

/**
 * Проверка капитала компании
 */
@Component
public class CapitalFilter implements ScoringValidator {
    /**
     * Величина капитала компании, ниже которой невозможно пройти скоринг
     */
    private static final int CAPITAL_MIN_VALUE = 5_000_000;

    @Override
    public void validate(ScoringStartRequest request, DelegateExecution execution) {
        Optional.ofNullable(request.getCapital())
                .ifPresentOrElse(capital -> {
                            if (capital < CAPITAL_MIN_VALUE) {
                                Classifier.setVariable(execution, IS_CAPITAL_LOW, Boolean.TRUE);
                                updateContext(execution);
                            } else {
                                Classifier.setVariable(execution, IS_CAPITAL_LOW, Boolean.FALSE);
                            }
                        },
                        () -> {
                            Classifier.setVariable(execution, IS_CAPITAL_LOW, Boolean.TRUE);
                            updateContext(execution);
                        });
    }

    @Override
    public void updateContext(DelegateExecution execution) {
        Context context = Classifier.getVariable(execution, CONTEXT);
        context.getScoringResult().remove(SCORING_SUCCESS);
        context.getScoringResult().add(CAPITAL_FAILURE);
        Classifier.setVariable(execution, CONTEXT, context);
    }
}
