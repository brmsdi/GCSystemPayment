package system.gc.dtos;

import lombok.Data;
import lombok.NonNull;
import java.util.List;

@Data
public class ErrorInfoDTO {
    private ErrorDTO errorDTO;
    private List<String> infos;

    public ErrorInfoDTO(ErrorDTO errorDTO) {
        this.errorDTO = errorDTO;
    }
    public ErrorInfoDTO(@NonNull ErrorDTO errorDTO, @NonNull List<String> infos)
    {
        this.errorDTO = errorDTO;
        this.infos = infos;
    }
}
