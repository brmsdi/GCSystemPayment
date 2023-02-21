package system.gc.dtos;

import lombok.Data;

@Data
public class ErrorDTO {
    private Integer status;
    private String message;

    public ErrorDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorDTO(String message) {
        this.message = message;
    }
}
