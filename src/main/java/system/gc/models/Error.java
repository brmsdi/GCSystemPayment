package system.gc.models;

import lombok.Data;

@Data
public class Error {
    private Integer status;
    private String message;

    public Error(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Error(String message) {
        this.message = message;
    }
}
