package com.tourGuide.tourGuide.config;

import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig implements ErrorDecoder {
   @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        Exception exception = defaultErrorDecoder.decode(s, response);

        if(exception instanceof RetryableException){
            return exception;
        }

        if(response.status() == 504){
            return exception;
        }

        return exception;
    }
}
