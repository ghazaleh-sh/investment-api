package ir.co.sadad.investment.dto.finodad;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
public class FundBasicReqDto implements Serializable {
    /**
     * شناسه صندوق سرمایه گذاری
     */
    private String fundId;

    /**
     * کد ملی مشتری
     */
    private String nationalCode;
}
