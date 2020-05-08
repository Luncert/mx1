package org.luncert.mx1.core.db.es.repo;

import org.luncert.mx1.core.db.es.entity.AppLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppLogRepo extends ElasticsearchCrudRepository<AppLog, String> {
}
