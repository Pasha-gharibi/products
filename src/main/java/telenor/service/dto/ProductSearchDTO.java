package telenor.service.dto;

public class ProductSearchDTO {
    private String type;
    private Integer min_price;
    private Integer max_price;
    private String city;
    private String property;
    private String color;
    private Integer gb_limit_min;
    private Integer gb_limit_max;

    public ProductSearchDTO() {
    }

    public ProductSearchDTO(String type,
                            Integer min_price,
                            Integer max_price,
                            String city,
                            String property,
                            String color,
                            Integer gb_limit_min,
                            Integer gb_limit_max) {
        this.type = type;
        this.min_price = min_price;
        this.max_price = max_price;
        this.city = city;
        this.property = property;
        this.color = color;
        this.gb_limit_min = gb_limit_min;
        this.gb_limit_max = gb_limit_max;
    }


    public String getType() {
        return type;
    }

    public Integer getMin_price() {
        return min_price;
    }

    public Integer getMax_price() {
        return max_price;
    }

    public String getCity() {
        return city;
    }

    public String getProperty() {
        return property;
    }

    public String getColor() {
        return color;
    }

    public Integer getGb_limit_min() {
        return gb_limit_min;
    }

    public Integer getGb_limit_max() {
        return gb_limit_max;
    }

}
