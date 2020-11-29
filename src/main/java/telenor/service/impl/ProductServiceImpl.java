package telenor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telenor.domain.Product;
import telenor.repository.ProductRepository;
import telenor.service.ProductService;
import telenor.service.dto.ProductSearchDTO;
import telenor.service.dto.ProductSearchResponseDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Return a {@link List} of {@link Product} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductSearchResponseDTO> findByCriteria(ProductSearchDTO criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSearchSpecification(criteria);
        return productRepository.findAll(specification).stream().map(t -> new ProductSearchResponseDTO(t)).collect(Collectors.toList());
    }


    /**
     * Function to convert {@link ProductSearchDTO} to a {@link Specification}
     *
     * @param filter The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    private Specification<Product> createSearchSpecification(ProductSearchDTO filter) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (filter.getType() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("type"), filter.getType()));
                }
                if (filter.getMin_price() != null) {
                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMin_price()));
                }
                if (filter.getMax_price() != null) {
                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("price"), filter.getMax_price()));
                }
                if (filter.getCity() != null) {
                    predicate.getExpressions().add(cb.like(root.get("store_address"), "%" + filter.getCity() + "%"));
                }
//                properties filed sample data  :  "properties": "gb_limit:50"    or   "color:purpur"
                if (filter.getProperty() != null) {
                    predicate.getExpressions().add(cb.like(root.get("properties"), "%:" + filter.getProperty() + "%"));
                }
                if (filter.getColor() != null) {
                    predicate.getExpressions().add(cb.like(root.get("properties"), "%:" + filter.getColor()));
                }
                if (filter.getGb_limit_min() != null) {
                    predicate.getExpressions().add(
                            cb.and(cb.like(root.get("properties"), "gb_limit:%"),
                                    cb.greaterThanOrEqualTo(cb.substring(root.get("properties"), 10, 2).as(String.class), filter.getGb_limit_min().toString())));
                }
                if (filter.getGb_limit_max() != null) {
                    predicate.getExpressions().add(cb.and(cb.like(root.get("properties"), "gb_limit:%"),
                            cb.lessThanOrEqualTo(cb.substring(root.get("properties"), 10).as(String.class), filter.getGb_limit_max().toString())));
                }

                return predicate;

            }

        };

    }

}
