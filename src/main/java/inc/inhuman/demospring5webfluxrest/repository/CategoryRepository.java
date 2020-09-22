package inc.inhuman.demospring5webfluxrest.repository;

import inc.inhuman.demospring5webfluxrest.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category,String>{

}
