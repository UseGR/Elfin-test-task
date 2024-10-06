package ru.scoring.camunda_app.process.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.scoring.api_specifications.context_api_controller._1_0_0.client.model.ContextSaveRequest;
import ru.scoring.camunda_app.process.main.variable.Context;
import ru.scoring.camunda_app.process.main.variable.ScoringResult;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ContextApiMapper {
    @Mapping(target = "description", source = "context.scoringResult")
    @Mapping(target = "scoring", source = "isScoringFailed")
    ContextSaveRequest mapContext(Context context, UUID processId, Boolean isScoringFailed);

    default List<String> mapScoringResult(List<ScoringResult> scoringResult) {
        return scoringResult.stream()
                .map(ScoringResult::getDescription)
                .toList();
    }
}
