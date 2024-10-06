package ru.scoring.context_api.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.scoring.context_api.entity.Context;

@Repository
public interface ContextRepository extends ElasticsearchRepository<Context, String> {
}
