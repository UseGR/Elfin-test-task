package ru.scoring.camunda_app.service.filter.impl;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.variables.Classifier;
import ru.scoring.camunda_app.process.main.variable.Context;
import ru.scoring.camunda_app.service.filter.ScoringValidator;

import java.util.Optional;

import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.CONTEXT;
import static ru.scoring.camunda_app.process.main.variable.MainProcessVariables.IS_RESIDENT_COMPANY;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.RESIDENTIAL_FAILURE;
import static ru.scoring.camunda_app.process.main.variable.ScoringResult.SCORING_SUCCESS;

/**
 * Проверка, является ли компания резидентом РФ
 */
@Component
public class ResidentialFilter implements ScoringValidator {
    /**
     * У компаний нерезидентов РФ ИНН начинается с 9909
     */
    private static final String NON_RESIDENT_COMPANY_TAXPAYER_NUMBER_PREFIX = "9909";

    @Override
    public void validate(ScoringStartRequest request, DelegateExecution execution) {
        Optional.ofNullable(request.getIndividualTaxpayerNumber())
                .ifPresentOrElse(number -> {
                            if (number.startsWith(NON_RESIDENT_COMPANY_TAXPAYER_NUMBER_PREFIX)) {
                                Classifier.setVariable(execution, IS_RESIDENT_COMPANY, Boolean.FALSE);
                                updateContext(execution);
                            } else {
                                Classifier.setVariable(execution, IS_RESIDENT_COMPANY, Boolean.TRUE);
                            }
                        },
                        () -> {
                            Classifier.setVariable(execution, IS_RESIDENT_COMPANY, Boolean.FALSE);
                            updateContext(execution);
                        });
    }

    @Override
    public void updateContext(DelegateExecution execution) {
        Context context = Classifier.getVariable(execution, CONTEXT);
        context.getScoringResult().remove(SCORING_SUCCESS);
        context.getScoringResult().add(RESIDENTIAL_FAILURE);
        Classifier.setVariable(execution, CONTEXT, context);
    }
}
