package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.ImageModel;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class ImageModelListener extends AbstractMongoEventListener<ImageModel> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public ImageModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<ImageModel> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(ImageModel.SEQUENCE_NAME));
        }
    }

}