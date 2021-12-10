package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.SubCategory;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class SubCategoryModelListener extends AbstractMongoEventListener<SubCategory> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public SubCategoryModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<SubCategory> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(SubCategory.SEQUENCE_NAME));
        }
    }


}