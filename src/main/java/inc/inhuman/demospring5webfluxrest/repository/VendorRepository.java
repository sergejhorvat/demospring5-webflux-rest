package inc.inhuman.demospring5webfluxrest.repository;

import inc.inhuman.demospring5webfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String>{
}
