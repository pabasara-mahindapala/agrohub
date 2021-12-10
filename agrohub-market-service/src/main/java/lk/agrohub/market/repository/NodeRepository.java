package lk.agrohub.market.repository;

import lk.agrohub.market.model.Node;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NodeRepository extends MongoRepository<Node, Long> {

    List<Node> findByJourneyId(long journeyId);
}
