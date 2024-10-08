package ir.co.sadad.investment.common.exception.handler.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class GeneralErrorResponseDto {

    private HttpStatusCode status;
    private String timestamp;
    private String code;
    private String message;
    private String localizedMessage;
    private List<GeneralSubErrorResponseDto> subErrors = new ArrayList<>();
    private Object extraData;

}
