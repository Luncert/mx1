package org.luncert.mx1.core.db.mongo.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class ProbeMetadata implements Serializable {
  
  private static final long serialVersionUID = 3393682389391439595L;
  
  /**
   * cipher of probe net address
   */
  @Id
  private ObjectId id;
  
  
}
