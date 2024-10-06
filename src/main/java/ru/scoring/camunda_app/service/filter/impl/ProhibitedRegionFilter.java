package ru.scoring.camunda_app.service.filter.impl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.variable.Context;
import ru.scoring.camunda_app.service.filter.ScoringValidator;

import java.util.Optional;

import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.CONTEXT;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.IS_COMPANY_IN_PROHIBITED_REGION;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.PROHIBITED_REGION_FAILURE;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.SCORING_SUCCESS;

/**
 * Проверка, находится ли компания в запрещенном регионе
 */
@Component
public class ProhibitedRegionFilter implements ScoringValidator {
    /**
     * Номер запрещенного региона (Красноярский край)
     */
    private static final int PROHIBITED_REGION_NUMBER = 24;

    @Override
    public void validate(ScoringStartRequest request, DelegateExecution execution) {
        Optional.ofNullable(request.getRegionalCode())
                .ifPresentOrElse(regionalCode -> {
                            if (regionalCode == PROHIBITED_REGION_NUMBER) {
                                Classifier.setVariable(execution, IS_COMPANY_IN_PROHIBITED_REGION, Boolean.TRUE);
                                updateContext(execution);
                            } else {
                                Classifier.setVariable(execution, IS_COMPANY_IN_PROHIBITED_REGION, Boolean.FALSE);
                            }
                        },
                        () -> {
                            Classifier.setVariable(execution, IS_COMPANY_IN_PROHIBITED_REGION, Boolean.TRUE);
                            updateContext(execution);
                        });
    }

    @Override
    public void updateContext(DelegateExecution execution) {
        Context context = Classifier.getVariable(execution, CONTEXT);
        context.getScoringResult().remove(SCORING_SUCCESS);
        context.getScoringResult().add(PROHIBITED_REGION_FAILURE);
        Classifier.setVariable(execution, CONTEXT, context);
    }
}
