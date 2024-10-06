package ru.scoring.context_api.mapper;

import org.mapstruct.Mapper;
import ru.scoring.api_specifications.context_api_controller._1_0_0.server.model.ContextGetResponse;
import ru.scoring.api_specifications.context_api_controller._1_0_0.server.model.ContextSaveRequest;
import ru.scoring.context_api.entity.Context;

@Mapper
public interface ContextMapper {
    Context mapContext(ContextSaveRequest request);

    ContextGetResponse mapContext(Context context);
}
