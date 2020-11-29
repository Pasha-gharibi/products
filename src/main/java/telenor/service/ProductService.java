package telenor.service;

import telenor.domain.Product;
import telenor.service.dto.ProductSearchDTO;
import telenor.service.dto.ProductSearchResponseDTO;

import java.util.List;

/**
 * Service Interface for managing {@link Product}.
 */
public interface ProductService {


    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    List<ProductSearchResponseDTO> findByCriteria(ProductSearchDTO dto);

}
