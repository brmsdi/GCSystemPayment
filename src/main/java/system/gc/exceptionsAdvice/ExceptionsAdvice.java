package system.gc.exceptionsAdvice;

import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import system.gc.models.Error;

@Log4j2
//@ControllerAdvice
public class ExceptionsAdvice {
/*
    @ExceptionHandler(GerencianetException.class)
    public Mono<ResponseEntity<Error>> gerencianetException(GerencianetException exception)
    {
        log.error(exception.getMessage());
        log.error(exception.getError());
        log.error(exception.getErrorDescription());
        log.error(exception.getCause());
        return Mono.just(ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST.value(), exception.getErrorDescription())));
    }

    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<Error>> illegalStateException(IllegalStateException exception)
    {
        log.error(exception.getMessage());
        log.error(exception.getCause());
        return Mono.just(ResponseEntity.badRequest().body(new Error(exception.getMessage())));
    } */
}
