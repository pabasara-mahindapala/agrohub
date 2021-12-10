package lk.agrohub.market.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lk.agrohub.market.model.Product;
import lk.agrohub.market.service.SequenceGeneratorService;

@Component
public class ProductModelListener extends AbstractMongoEventListener<Product> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public ProductModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Product> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Product.SEQUENCE_NAME));
        }
    }

}