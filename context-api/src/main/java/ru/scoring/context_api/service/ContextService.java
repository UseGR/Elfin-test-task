package ru.scoring.context_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.scoring.context_api.entity.Context;
import ru.scoring.context_api.exception.ContextNotFoundException;
import ru.scoring.context_api.repository.ContextRepository;

@Service
@RequiredArgsConstructor
public class ContextService {
    private final ContextRepository contextRepository;

    public void saveContext(Context context) {
        contextRepository.save(context);
    }

    public Context getContextByProcessId(String processId) {
        return contextRepository.findById(processId).orElseThrow(() -> new ContextNotFoundException("Контекст по идентификатору " + processId + " не найден"));
    }

}
