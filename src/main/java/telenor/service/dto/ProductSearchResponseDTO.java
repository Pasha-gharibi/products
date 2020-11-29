package telenor.service.dto;

import telenor.domain.Product;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductSearchResponseDTO implements Serializable {

    private String type;
    private String properties;
    private BigDecimal price;
    private String store_address;

    public ProductSearchResponseDTO() {
    }

    public ProductSearchResponseDTO(Product product) {
        this.type = product.getType();
        this.price = product.getPrice();
        this.properties = product.getProperties();
        this.store_address = product.getStore_address();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }


}
