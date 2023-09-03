package com.polarbookshop.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {
    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }
    public Mono<Book> getBookByIsbn(String isbn){
        return webClient.get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(3), Mono.empty()) // Defining timeout and fallback for the HTTP interaction
                .onErrorResume(WebClientRequestException.class, exception -> Mono.empty()) // Returns an empty object when a 404 response is received
                .retryWhen(
                        Retry.backoff(3, Duration.ofMillis(100)) //
                ) // Exponential backoff is used as the retry strategy. Three attempts are allowed with a 100 ms initial backoff.
                .onErrorResume(exception -> Mono.empty()); // If any error happens after the 3 retry attempts, catch the exception and return an empty object.
    }
}
