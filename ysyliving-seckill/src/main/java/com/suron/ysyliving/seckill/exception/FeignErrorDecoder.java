package com.suron.ysyliving.seckill.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * @author Suron
 * @version 1.0
 */

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int statusCode = response.status();
        switch (statusCode) {
            case 400:
                return new BadRequestException("Bad request");
            case 404:
                return new NotFoundException("Not found");
            default:
                return new Exception("Generic error");
        }
    }

    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}