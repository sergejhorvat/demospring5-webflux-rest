package inc.inhuman.demospring5webfluxrest.controllers;

import inc.inhuman.demospring5webfluxrest.domain.Category;
import inc.inhuman.demospring5webfluxrest.domain.Vendor;
import inc.inhuman.demospring5webfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

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
}