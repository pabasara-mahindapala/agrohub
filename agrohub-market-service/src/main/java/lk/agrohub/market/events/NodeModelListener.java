package lk.agrohub.market.events;

import lk.agrohub.market.model.Node;
import lk.agrohub.market.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class NodeModelListener extends AbstractMongoEventListener<Node> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public NodeModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Node> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Node.SEQUENCE_NAME));
        }
    }


}