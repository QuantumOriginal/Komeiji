package org.qo.komeiji.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Request {
    public enum Method {
        GET, POST
    }

    /**
     * Sends an HTTP request asynchronously.
     *
     * @param method      The HTTP method (GET or POST).
     * @param url         The URL to which the request is sent.
     * @param requestBody The request body, used only for POST requests. Can be null.
     * @param headers     A map of request headers. Can be null.
     * @return A CompletableFuture that completes with the response body as a String if the request is successful,
     *         or null if the request fails or receives a non-200 status code.
     * @throws IllegalArgumentException If an unsupported HTTP method is used.
     */
    public static CompletableFuture<String> sendRequest(Method method, String url, String requestBody, Map<String, String> headers) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }

        HttpRequest request;
        try {
            switch (method) {
                case GET:
                    request = requestBuilder.GET().build();
                    break;
                case POST:
                    request = requestBuilder.POST(BodyPublishers.ofString(requestBody == null ? "" : requestBody)).build();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method");
            }

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.body();
                        } else {
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }
    public static CompletableFuture<String> sendRequest(Method method, String url, String requestBody) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        HttpRequest request;
        try {
            switch (method) {
                case GET:
                    request = requestBuilder.GET().build();
                    break;
                case POST:
                    request = requestBuilder.POST(BodyPublishers.ofString(requestBody == null ? "" : requestBody)).build();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method");
            }

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        if (response.statusCode() == 200) {
                            return response.body();
                        } else {
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }
}
