package org.luncert.mx1.core.db.es.repo;

import org.luncert.mx1.core.db.es.entity.DynamicSysInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDynamicSysInfoRepo extends ElasticsearchCrudRepository<DynamicSysInfo, String> {
}
