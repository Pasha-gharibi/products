package telenor.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import telenor.rest.wrapper.DataWrapper;
import telenor.service.ProductService;
import telenor.service.dto.ProductSearchDTO;
import telenor.service.dto.ProductSearchResponseDTO;

import java.util.List;

/**
 * REST controller for managing {@link telenor.domain.Product}.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @param type         the type criteria which the product should match. values : -phone -subscription
     * @param min_price    the min price criteria which the product should match.
     * @param max_price    the max price criteria which the product should match.
     * @param city         the city criteria which the product should match.
     * @param property     the criteria which the product should match. values :-gb_limit -color
     * @param color        the color value of property criteria which the product should match. ( acceptable only if property=color )
     * @param gb_limit_min the gb limit min value of property criteria which the product should match.( acceptable only if property=gb_limit )
     * @param gb_limit_max the gb limit max value of property criteria criteria which the product should match.( acceptable only if property=gb_limit )
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */

    @GetMapping("/product")
    public ResponseEntity<DataWrapper> getAllProducts(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "min_price", required = false) Integer min_price,
            @RequestParam(value = "max_price", required = false) Integer max_price,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "property", required = false) String property,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "gb_limit_min", required = false) Integer gb_limit_min,
            @RequestParam(value = "gb_limit_max", required = false) Integer gb_limit_max) {
        ProductSearchDTO searchDto = new ProductSearchDTO(type, min_price, max_price, city, property, color, gb_limit_min, gb_limit_max);
        List<ProductSearchResponseDTO> entityList = productService.findByCriteria(searchDto);
        DataWrapper wrapper = new DataWrapper(entityList);
        return ResponseEntity.ok().body(wrapper);
    }

}
