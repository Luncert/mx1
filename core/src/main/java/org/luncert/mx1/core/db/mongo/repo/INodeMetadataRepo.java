package org.luncert.mx1.core.db.mongo.repo;

import org.luncert.mx1.core.db.mongo.entity.NodeMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INodeMetadataRepo extends CrudRepository<NodeMetadata, String> {
}
