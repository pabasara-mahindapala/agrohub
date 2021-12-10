package lk.agrohub.market.dtos;

import java.io.Serializable;
import java.util.List;

public class Route {
    private List<Long> orderIdsRouted;

    public Route() {
    }

    public Route(List<Long> orderIdsRouted) {
        this.orderIdsRouted = orderIdsRouted;
    }

    public List<Long> getOrderIdsRouted() {
        return orderIdsRouted;
    }

    public void setOrderIdsRouted(List<Long> orderIdsRouted) {
        this.orderIdsRouted = orderIdsRouted;
    }
}
