package io.jenkins.plugins.kobiton.shared.utils;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Flow;

public class StringSubscriber implements Flow.Subscriber<ByteBuffer> {

    final HttpResponse.BodySubscriber<String> wrapped;

    StringSubscriber(HttpResponse.BodySubscriber<String> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        wrapped.onSubscribe(subscription);
    }
    @Override
    public void onNext(ByteBuffer item) {
        wrapped.onNext(List.of(item));
    }
    @Override
    public void onError(Throwable throwable) {
        wrapped.onError(throwable);
    }
    @Override
    public void onComplete() {
        wrapped.onComplete();
    }

    public static String wrapBodyPublisher(HttpRequest.BodyPublisher p) {
        var bodySubscriber = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        var flowSubscriber = new StringSubscriber(bodySubscriber);
        p.subscribe(flowSubscriber);
        return bodySubscriber.getBody().toCompletableFuture().join();
    }
}