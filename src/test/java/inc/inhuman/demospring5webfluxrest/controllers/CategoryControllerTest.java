package inc.inhuman.demospring5webfluxrest.controllers;

import inc.inhuman.demospring5webfluxrest.domain.Category;
import inc.inhuman.demospring5webfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();

    }

    @Test
    void list() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder()
                                .description("Cat1")
                                .build(),
                        Category.builder()
                                .description("Cat2")
                                .build()
                ));

        webTestClient.get().uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    void getById() {

        BDDMockito.given(categoryRepository.findById("someid"))
                .willReturn(Mono.just(Category.builder()
                        .description("Cat")
                        .build()
                ));

        webTestClient.get().uri("/api/v1/categories/someid/")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void testCreateCategory(){
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some category").build());
        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSaveMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdate(){
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.
                        just(Category
                                .builder()
                                .build()
                        )
                );

        Mono<Category> catToUpdate = Mono.just(Category.builder()
                .description("Some cat to update")
                .build());

        webTestClient.put()
                .uri(("/api/v1/categories/soem"))
                .body(catToUpdate,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatchNoChange(){
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.
                        just(Category
                                .builder()
                                .build()
                        )
                );

        Mono<Category> catToUpdate = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri(("/api/v1/categories/soem"))
                .body(catToUpdate,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository, never()).save(any());
    }



    @Test
    void testPatchWithChange(){
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.
                        just(Category
                                .builder()
                                .build()
                        )
                );

        Mono<Category> catToUpdate = Mono.just(Category.builder()
                .description("Some cat to update")
                .build());

        webTestClient.patch()
                .uri(("/api/v1/categories/soem"))
                .body(catToUpdate,Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository).save(any());
    }
}