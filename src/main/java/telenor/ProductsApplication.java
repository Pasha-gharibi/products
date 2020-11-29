package telenor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import telenor.domain.Product;
import telenor.repository.ProductRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@SpringBootApplication
public class ProductsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext cac = SpringApplication.run(ProductsApplication.class, args);
        ProductRepository pr = cac.getBean(ProductRepository.class);
        initData(pr);

    }

    public static void initData(ProductRepository pr) {
        List<List<String>> records = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        try (Scanner scanner = new Scanner(new ClassPathResource("data.csv").getFile())) {
            scanner.nextLine(); // pass the csv header! :)
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (List<String> record : records) {
            products.add(new Product(record.get(0), record.get(1), new BigDecimal(record.get(2)), record.get(3).replace("\"", "")));
        }
        pr.saveAll(products);

    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }


}
