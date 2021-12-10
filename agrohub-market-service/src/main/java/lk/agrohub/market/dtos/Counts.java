package lk.agrohub.market.dtos;

import lk.agrohub.market.model.User;

import java.util.List;

public class Counts {
    private long farmersCount;
    private long buyersCount;
    private long productsCount;
    private long ordersCount;

    public Counts(long farmersCount, long buyersCount, long productsCount, long ordersCount) {
        this.farmersCount = farmersCount;
        this.buyersCount = buyersCount;
        this.productsCount = productsCount;
        this.ordersCount = ordersCount;
    }

    public long getFarmersCount() {
        return farmersCount;
    }

    public void setFarmersCount(long farmersCount) {
        this.farmersCount = farmersCount;
    }

    public long getBuyersCount() {
        return buyersCount;
    }

    public void setBuyersCount(long buyersCount) {
        this.buyersCount = buyersCount;
    }

    public long getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(long productsCount) {
        this.productsCount = productsCount;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }
}
