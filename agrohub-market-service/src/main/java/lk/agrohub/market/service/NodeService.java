package lk.agrohub.market.service;

import lk.agrohub.market.model.Node;
import lk.agrohub.market.model.SubCategory;
import lk.agrohub.market.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    @Autowired
    NodeRepository nodeRepository;

    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    public List<Node> getAllNodesByJourneyId(long journeyId) {
        return nodeRepository.findByJourneyId(journeyId);
    }

    public Node nodeSearchById(long _id) {
        return nodeRepository.findById(_id).orElse(null);
    }

    public Node createNode(Node node) throws Exception {
        return this.nodeRepository.save(node);
    }

    public Node updateNode(Node node) {
        return this.nodeRepository.save(node);
    }

    public void deleteNode(Node node) {
        this.nodeRepository.delete(node);
    }

}
