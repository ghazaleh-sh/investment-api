package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

/**
 * error obj of Finodad
 * <pre>
 *     response of Finodad is 200 by default
 *     in case of exception this obj will be use
 *     but with Status code of 200
 * </pre>
 */
@Data
public class ErrorDto {
    private String message;
    private Integer code;
}
