package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.Journey;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class JourneyModelListener extends AbstractMongoEventListener<Journey> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public JourneyModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Journey> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Journey.SEQUENCE_NAME));
        }
    }


}