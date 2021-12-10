package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.Vehicle;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class VehicleModelListener extends AbstractMongoEventListener<Vehicle> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public VehicleModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Vehicle> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Vehicle.SEQUENCE_NAME));
        }
    }


}