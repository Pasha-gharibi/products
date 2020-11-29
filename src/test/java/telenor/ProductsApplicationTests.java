package telenor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import telenor.domain.Product;
import telenor.repository.ProductRepository;
import telenor.rest.wrapper.DataWrapper;
import telenor.service.dto.ProductSearchDTO;
import telenor.service.dto.ProductSearchResponseDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@JsonDeserialize(as = ProductSearchResponseDTO.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductsApplication.class)
@WebAppConfiguration
public class ProductsApplicationTests {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ProductRepository productRepository;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ProductsApplication.initData(productRepository);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }


    @Test
    public void getProductsListCheckEndpointIsUp() throws Exception {
        String uri = "/api/product";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        DataWrapper wrapper = mapFromJson(content, DataWrapper.class);
        assertTrue(wrapper.getData().size() > 0);
    }


    @Test
    public void testTypeParamWorksFine() throws Exception {
        String uri = "/api/product?type=phone";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        DataWrapper wrapper = mapFromJson(content, DataWrapper.class);

        Product p = new Product();
        p.setType("phone");
        List<Product> dbContent = productRepository.findAll(Example.of(p));
        assertEquals(dbContent.size(),wrapper.getData().size());

    }

    @Test
    public void param_Gb_limit_min_hasCorrectData() throws Exception {
        String uri = "/api/product?gb_limit_min=50";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        DataWrapper wrapper = mapFromJson(content, DataWrapper.class);
        List<ProductSearchResponseDTO> filter = wrapper.getData().stream().filter(p ->Integer.valueOf(p.getProperties().split(":")[1]) >=50).collect(Collectors.toList());
        assertEquals(filter.size(),wrapper.getData().size());

    }

    @Test
    public void caseSensitiveParamCityWorksFine() throws Exception {
        String uri = "/api/product?city=Stockholm";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        DataWrapper wrapper = mapFromJson(content, DataWrapper.class);

        List<Product> dbContent =productRepository.findAll(createSearchStockholmSpecification());
        assertEquals(dbContent.size(),wrapper.getData().size());

    }

    private Specification<Product> createSearchStockholmSpecification() {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.like(root.get("store_address"), "%Stockholm%"));
                return predicate;
            }

        };
    }

    @Test
    public void checkConjunctionOfUrlParamsWorkFine() throws Exception {
        String uri = "/api/product?type=subscription&max_price=1000&city=Stockholm";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        DataWrapper wrapper = mapFromJson(content, DataWrapper.class);

        List<Product> dbContent =productRepository.findAll(createSearchWithConjunctionSpecification());
        assertEquals(dbContent.size(),wrapper.getData().size());

    }

    private Specification<Product> createSearchWithConjunctionSpecification() {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.equal(root.get("type"), "subscription"));
                predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("price"), "1000"));
                predicate.getExpressions().add(cb.like(root.get("store_address"), "%Stockholm%"));
                return predicate;
            }

        };
    }


}
