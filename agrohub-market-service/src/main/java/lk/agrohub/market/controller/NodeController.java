package lk.agrohub.market.controller;

import lk.agrohub.market.model.Journey;
import lk.agrohub.market.model.Node;
import lk.agrohub.market.security.response.MessageResponse;
import lk.agrohub.market.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rest/node")
public class NodeController {
    private static final Logger logger = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    NodeService nodeService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<List<Node>> listAllNode(@RequestParam(required = false) Long journeyId) {
        try {
            if (journeyId != null) {
                return new ResponseEntity<>(nodeService.getAllNodesByJourneyId(journeyId), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(nodeService.getAllNodes(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Unable to get list", e);
            return new ResponseEntity(new MessageResponse("Unable to get list"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{nodeId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Node> getNode(@PathVariable long nodeId) {
        try {
            Node node = nodeService.nodeSearchById(nodeId);

            // throw exception if null
            if (node == null) {
                throw new RuntimeException("Node not found");
            }

            return new ResponseEntity<>(node, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to get node", e);
            return new ResponseEntity(new MessageResponse("Unable to get node"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Node> createNode(@RequestBody Node node) {
        ResponseEntity<Node> result;
        try {
            node = this.nodeService.createNode(node);
            result = new ResponseEntity<>(node, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to add node", e);
            result = new ResponseEntity(new MessageResponse("Unable to add node"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<Node> updateNode(@RequestBody Node node) {
        ResponseEntity<Node> result;
        try {
            node = this.nodeService.updateNode(node);
            result = new ResponseEntity<>(node, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to update node", e);
            result = new ResponseEntity(new MessageResponse("Unable to update node"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @DeleteMapping("/delete/{nodeId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER') or hasRole('TRANSPORTER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteNode(@PathVariable long nodeId) {
        try {
            Node node = nodeService.nodeSearchById(nodeId);

            // throw exception if null
            if (node == null) {
                throw new RuntimeException("Node not found");
            }

            nodeService.deleteNode(node);

            return new ResponseEntity<>(new MessageResponse("Deleted Node"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to delete node", e);
            return new ResponseEntity<>(new MessageResponse("Unable to delete node"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
