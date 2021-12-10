package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.Location;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class LocationModelListener extends AbstractMongoEventListener<Location> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public LocationModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Location> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Location.SEQUENCE_NAME));
        }
    }

}