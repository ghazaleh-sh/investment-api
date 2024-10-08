package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

/**
 * پاسخ سرویس استعلام وضعیت مشتری (صندوق و سجام)
 */
@Data
public class CustomerInquiryResDto implements Serializable {

    /**
     * اگر کدملی سجامی باشد (true) در غیر اینصورت (false)
     */
    private Boolean sejam;

    /**
     * اگر مشتری صندوق باشد (true) در غیر اینصورت (false)
     */
    private Boolean fund;
}
