package org.luncert.mx1.core.controller;

import org.luncert.mx1.core.dto.NodeListItemDto;
import org.luncert.mx1.core.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NodeController {
  
  @Autowired
  private INodeService nodeService;
  
  @GetMapping("/nodes")
  public ResponseEntity<List<NodeListItemDto>> getNodeList() {
    return new ResponseEntity<>(nodeService.getAllNodes(), HttpStatus.OK);
  }
  
  @GetMapping("/node/{nodeId}/metadata")
  public ResponseEntity getMetadata(@PathVariable String nodeId) {
    return ResponseEntity.of(nodeService.getNodeMetadata(nodeId));
  }
}
