package inc.inhuman.demospring5webfluxrest.controllers;

import inc.inhuman.demospring5webfluxrest.domain.Category;
import inc.inhuman.demospring5webfluxrest.domain.Vendor;
import inc.inhuman.demospring5webfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }


    @Test
    void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder()
                                .firstName("Najk")
                                .lastName("Majke")
                                .build(),
                        Vendor.builder()
                                .firstName("OsiSoft")
                                .lastName("AB")
                                .build()
                ));

        webTestClient.get().uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);

    }

    @Test
    void getById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder()
                                .firstName("Najk")
                                .lastName("Majke")
                                .build()
                ));

        webTestClient.get().uri("/api/v1/vendors/someid")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void testCreateVendor(){
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder()
                .firstName("nike")
                .lastName("patike")
                .build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSave,Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdateVendor(){
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono
                        .just(Vendor
                                .builder()
                                .build()
                        )
                );

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder()
                                                .firstName("Nike")
                                                .lastName("patike")
                                                .build()
                                                );
        webTestClient.put()
        .uri("/api/v1/vendors/someid")
        .body(vendorToUpdate, Vendor.class)
        .exchange()
        .expectStatus()
        .isOk();

    }

    @Test
    void testPatchNoChange(){
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository, Mockito.never()).save(any());
    }

    @Test
    void testPatchWithChange(){
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Jimmy").build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("Nike").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/someid")
                .body(vendorToUpdate,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository).save(any());
    }

}