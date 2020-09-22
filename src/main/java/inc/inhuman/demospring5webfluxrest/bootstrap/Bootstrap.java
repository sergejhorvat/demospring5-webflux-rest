package inc.inhuman.demospring5webfluxrest.bootstrap;

import inc.inhuman.demospring5webfluxrest.domain.Category;
import inc.inhuman.demospring5webfluxrest.domain.Vendor;
import inc.inhuman.demospring5webfluxrest.repository.CategoryRepository;
import inc.inhuman.demospring5webfluxrest.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository  categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if(categoryRepository.count().block()== 0){
            // load data
            System.out.println("##### Loading data with bootstrap ######");

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();

            System.out.println("Loaded categories:" + categoryRepository.count().block());

            vendorRepository.save(Vendor.builder().
                    firstName("Joe")
                    .lastName("Doe")
                    .build()).block();

            vendorRepository.save(Vendor.builder().
                    firstName("Robin")
                    .lastName("Hood")
                    .build()).block();

            vendorRepository.save(Vendor.builder().
                    firstName("Alen")
                    .lastName("Osqald")
                    .build()).block();

            vendorRepository.save(Vendor.builder().
                    firstName("Julian")
                    .lastName("Asange")
                    .build()).block();

            System.out.println("Vendor repository loaded " + vendorRepository.count().block());

        }

    }
}
