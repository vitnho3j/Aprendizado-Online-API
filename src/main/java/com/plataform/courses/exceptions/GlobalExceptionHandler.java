package com.plataform.courses.exceptions;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.plataform.courses.services.exceptions.BadWordException;
import com.plataform.courses.services.exceptions.BuyerEqualsToAuthorException;
import com.plataform.courses.services.exceptions.CourseInactiveUpdateException;
import com.plataform.courses.services.exceptions.CreateCourseWithAuthorInativeException;
import com.plataform.courses.services.exceptions.CreatePurchaseWithBuyerInactive;
import com.plataform.courses.services.exceptions.CreatePurchaseWithCourseInactive;
import com.plataform.courses.services.exceptions.CreateSaleWithCourseInactive;
import com.plataform.courses.services.exceptions.CreateSaleWithSellerInactive;
import com.plataform.courses.services.exceptions.DataBindingViolationException;
import com.plataform.courses.services.exceptions.DuplicatePurchaseException;
import com.plataform.courses.services.exceptions.NotPermissionImmutableData;
import com.plataform.courses.services.exceptions.ObjectNotFoundException;
import com.plataform.courses.services.exceptions.SellerNotEqualsToAuthorException;
import com.plataform.courses.services.exceptions.UserInactiveUpdateException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${server.error.include-exception}")
    private boolean printStackTrace;

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull
            MethodArgumentNotValidException methodArgumentNotValidException,
            @NonNull
            HttpHeaders headers,
            @NonNull
            HttpStatusCode status,
            @NonNull
            WebRequest request) {
        List<String> errorMessages = new ArrayList<>();
        
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            String errorMessage = String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
            errorMessages.add(errorMessage);
        }
        
        String detailedMessage = String.join(", ", errorMessages);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                detailedMessage);
        
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        final String errorMessage = "Unknown error occurred";
        log.error(errorMessage, exception);
        return buildErrorResponse(
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(BadWordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadWordException(
            BadWordException badWordException,
            WebRequest request) {
        log.error("A bad word was founded it!", badWordException);
        return buildErrorResponse(
            badWordException,
            HttpStatus.BAD_REQUEST,
            request);   
    }

    @ExceptionHandler(CreateCourseWithAuthorInativeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCreateCourseWithAuthorInativeException(
            CreateCourseWithAuthorInativeException createCourseWithAuthorInativeException,
            WebRequest request) {
        log.error("You cannot create a Course for an inactive Author!", createCourseWithAuthorInativeException);
        return buildErrorResponse(
            createCourseWithAuthorInativeException,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(UserInactiveUpdateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleUserInactiveUpdateException(
            UserInactiveUpdateException userInactiveUpdateException,
            WebRequest request) {
        log.error("You cannot update an inactive user!", userInactiveUpdateException);
        return buildErrorResponse(
            userInactiveUpdateException,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(CourseInactiveUpdateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCourseInactiveUpdateException(
            CourseInactiveUpdateException courseInactiveUpdateException,
            WebRequest request) {
        log.error("You cannot update an inactive course!", courseInactiveUpdateException);
        return buildErrorResponse(
            courseInactiveUpdateException,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(CreatePurchaseWithBuyerInactive.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCreatePurchaseWithBuyerInactive(
            CreatePurchaseWithBuyerInactive createPurchaseWithBuyerInactive,
            WebRequest request) {
        log.error("You cannot Purchase a course for an inactive User!", createPurchaseWithBuyerInactive);
        return buildErrorResponse(
            createPurchaseWithBuyerInactive,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(CreatePurchaseWithCourseInactive.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCreatePurchaseWithCourseInactive(
            CreatePurchaseWithCourseInactive createPurchaseWithCourseInactive,
            WebRequest request) {
        log.error("You cannot create a Purchase for an inactive Course!", createPurchaseWithCourseInactive);
        return buildErrorResponse(
            createPurchaseWithCourseInactive,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(CreateSaleWithSellerInactive.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCreateSaleWithSellerInactive(
            CreateSaleWithSellerInactive createSaleWithSellerInactive,
            WebRequest request) {
        log.error("You cannot create a Sale for an inactive Seller!", createSaleWithSellerInactive);
        return buildErrorResponse(
            createSaleWithSellerInactive,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(CreateSaleWithCourseInactive.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleCreateSaleWithCourseInactive(
            CreateSaleWithCourseInactive createSaleWithCourseInactive,
            WebRequest request) {
        log.error("You cannot create a Sale for an inactive Course!", createSaleWithCourseInactive);
        return buildErrorResponse(
            createSaleWithCourseInactive,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(DuplicatePurchaseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicatePurchaseException(
            DuplicatePurchaseException duplicateException,
            WebRequest request) {
        log.error("this user already buyed this course!", duplicateException);
        return buildErrorResponse(
            duplicateException,
            HttpStatus.CONFLICT,
            request);   
    }

    @ExceptionHandler(SellerNotEqualsToAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleSellerNotEqualsToAuthorException(
            SellerNotEqualsToAuthorException sellerNotEqualsToAuthorException,
            WebRequest request) {
        log.error("The seller is not the author of the course!", sellerNotEqualsToAuthorException);
        return buildErrorResponse(
            sellerNotEqualsToAuthorException,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(NotPermissionImmutableData.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleNotPermissionImmutableData(
            NotPermissionImmutableData notPermissionImmutableData,
            WebRequest request) {
        log.error("You don't have permission to manage this data!", notPermissionImmutableData);
        return buildErrorResponse(
            notPermissionImmutableData,
            HttpStatus.FORBIDDEN,
            request);   
    }

    @ExceptionHandler(BuyerEqualsToAuthorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleBuyerEqualsToAuthorException(
            BuyerEqualsToAuthorException buyerEqualsToAuthorException,
            WebRequest request) {
        log.error("A seller cannot buy their own course!", buyerEqualsToAuthorException);
        return buildErrorResponse(
            buyerEqualsToAuthorException,
            HttpStatus.FORBIDDEN,
            request);   
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        return buildErrorResponse(
                dataIntegrityViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {
        log.error("Failed to validate element", constraintViolationException);
        return buildErrorResponse(
                constraintViolationException,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {
        log.error("Failed to find the requested element", objectNotFoundException);
        return buildErrorResponse(
                objectNotFoundException,
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        return buildErrorResponse(
                dataBindingViolationException,
                HttpStatus.CONFLICT,
                request);
    }

    // @ExceptionHandler(AuthenticationException.class)
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // public ResponseEntity<Object> handleAuthenticationException(
    //         AuthenticationException authenticationException,
    //         WebRequest request) {
    //     log.error("Authentication error ", authenticationException);
    //     return buildErrorResponse(
    //             authenticationException,
    //             HttpStatus.UNAUTHORIZED,
    //             request);
    // }

    // @ExceptionHandler(AccessDeniedException.class)
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    // public ResponseEntity<Object> handleAccessDeniedException(
    //         AccessDeniedException accessDeniedException,
    //         WebRequest request) {
    //     log.error("Authorization error ", accessDeniedException);
    //     return buildErrorResponse(
    //             accessDeniedException,
    //             HttpStatus.FORBIDDEN,
    //             request);
    // }

    // @ExceptionHandler(AuthorizationException.class)
    // @ResponseStatus(HttpStatus.FORBIDDEN)
    // public ResponseEntity<Object> handleAuthorizationException(
    //         AuthorizationException authorizationException,
    //         WebRequest request) {
    //     log.error("Authorization error ", authorizationException);
    //     return buildErrorResponse(
    //             authorizationException,
    //             HttpStatus.FORBIDDEN,
    //             request);
    // }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // @Override
    // public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
    //         AuthenticationException exception) throws IOException, ServletException {
    //     Integer status = HttpStatus.UNAUTHORIZED.value();
    //     response.setStatus(status);
    //     response.setContentType("application/json");
    //     ErrorResponse errorResponse = new ErrorResponse(status, "Username or password are invalid");
    //     response.getWriter().append(errorResponse.toJson());
    // }

}