package ru.scoring.context_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;
import ru.scoring.api_specifications.context_api_controller._1_0_0.server.api.ContextV10ApiController;
import ru.scoring.api_specifications.context_api_controller._1_0_0.server.model.ContextGetResponse;
import ru.scoring.api_specifications.context_api_controller._1_0_0.server.model.ContextSaveRequest;
import ru.scoring.context_api.entity.Context;
import ru.scoring.context_api.exception.ContextNotFoundException;
import ru.scoring.context_api.mapper.ContextMapper;
import ru.scoring.context_api.service.ContextService;

import java.util.UUID;

@Controller
public class ContextApiController extends ContextV10ApiController {

    private final ContextService contextService;
    private final ContextMapper contextMapper;

    public ContextApiController(NativeWebRequest request,
                                ContextMapper contextMapper,
                                ContextService contextService) {
        super(request);
        this.contextMapper = contextMapper;
        this.contextService = contextService;
    }

    @Override
    public ResponseEntity<ContextGetResponse> contextGet(UUID processId) {
        try {
            Context context = contextService.getContextByProcessId(processId.toString());

            return ResponseEntity.ok(contextMapper.mapContext(context));
        } catch (ContextNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> contextSave(ContextSaveRequest contextSaveRequest) {
        Context context = contextMapper.mapContext(contextSaveRequest);
        contextService.saveContext(context);

        return ResponseEntity.ok().build();
    }
}
