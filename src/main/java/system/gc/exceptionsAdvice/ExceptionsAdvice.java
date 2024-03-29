package system.gc.exceptionsAdvice;

import br.com.gerencianet.gnsdk.exceptions.GerencianetException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import system.gc.dtos.ErrorDTO;
import system.gc.dtos.ErrorInfoDTO;

@Log4j2
@ControllerAdvice
public class ExceptionsAdvice {
    @ExceptionHandler(GerencianetException.class)
    public ResponseEntity<ErrorInfoDTO> gerencianetException(GerencianetException exception)
    {
        log.error(exception.getMessage());
        log.error(exception.getError());
        log.error(exception.getErrorDescription());
        return ResponseEntity.badRequest().body(new ErrorInfoDTO(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getErrorDescription())));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorInfoDTO> illegalStateException(IllegalStateException exception)
    {
        log.error(exception.getMessage());
        log.error(exception.getCause());
        return ResponseEntity.badRequest().body(new ErrorInfoDTO(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), exception.getMessage())));
    }
}
