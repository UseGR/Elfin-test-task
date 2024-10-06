package ru.scoring.camunda_app.controller;

import ru.scoring.api_specifications.process_controller._1_0_0.server.model.ScoringStartRequest;
import ru.scoring.camunda_app.engine.variables.MetaData;
import ru.scoring.camunda_app.engine.variables.ProcessVariable;

public interface EventVariables {
    MetaData<ScoringStartRequest> START_SCORING = new ProcessVariable<>("start_scoring", ScoringStartRequest.class);
}
