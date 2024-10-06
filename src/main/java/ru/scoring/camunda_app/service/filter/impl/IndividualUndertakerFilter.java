package ru.scoring.camunda_app.service.filter.impl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.variable.Context;
import ru.scoring.camunda_app.service.filter.ScoringValidator;

import java.util.Optional;

import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.CONTEXT;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.IS_INDIVIDUAL_UNDERTAKER;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.INDIVIDUAL_UNDERTAKER_FAILURE;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.SCORING_SUCCESS;

/**
 * Проверка, является ли компания ИП
 */
@Component
public class IndividualUndertakerFilter implements ScoringValidator {
    /**
     * Длина ИНН ИП
     */
    private static final int INDIVIDUAL_UNDERTAKER_NUMBER_LENGTH = 12;

    @Override
    public void validate(ScoringStartRequest request, DelegateExecution execution) {
        Optional.ofNullable(request.getIndividualTaxpayerNumber())
                .ifPresentOrElse(number -> {
                            if (number.length() == INDIVIDUAL_UNDERTAKER_NUMBER_LENGTH) {
                                Classifier.setVariable(execution, IS_INDIVIDUAL_UNDERTAKER, Boolean.TRUE);
                                updateContext(execution);
                            } else {
                                Classifier.setVariable(execution, IS_INDIVIDUAL_UNDERTAKER, Boolean.FALSE);
                            }
                        },
                        () -> {
                            Classifier.setVariable(execution, IS_INDIVIDUAL_UNDERTAKER, Boolean.TRUE);
                            updateContext(execution);
                        });
    }

    @Override
    public void updateContext(DelegateExecution execution) {
        Context context = Classifier.getVariable(execution, CONTEXT);
        context.getScoringResult().remove(SCORING_SUCCESS);
        context.getScoringResult().add(INDIVIDUAL_UNDERTAKER_FAILURE);
        Classifier.setVariable(execution, CONTEXT, context);
    }
}
