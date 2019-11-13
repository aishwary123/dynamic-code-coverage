package com.dynamic.codecoverage.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dynamic.codecoverage.CodeCoverageApplication;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.ErrorResponse;

/**
 * This class is a controller advice. If certain exception is raised from
 * application which we have not handled this class will catch that one and will
 * pass a meaningful message to user.
 * 
 * @author aishwaryt
 */

@ControllerAdvice(basePackageClasses = CodeCoverageApplication.class)
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(final Exception exception) {

        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        exception);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(
                    Optional.of(exception.getClass().getSimpleName()).orElse(
                                CustomMessages.SERVER_SIDE_ISSUE));
        return new ResponseEntity<ErrorResponse>(errorResponse,
                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
