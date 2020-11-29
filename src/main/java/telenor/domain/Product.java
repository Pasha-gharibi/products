package telenor.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Product.
 */
@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    public Product() {

    }

    public Product(String type, String properties, BigDecimal price, String store_address) {
        this.type = type;
        this.properties = properties;
        this.price = price;
        this.store_address = store_address;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String properties;

    private BigDecimal price;

    private String store_address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
