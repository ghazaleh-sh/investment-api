package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

/**
 * درخواست سرویس درخواست صدور
 */
@Data
public class IssueCreationReqDto extends CreationBaseRequestDto {
    /**
     * روش صدور (0= حساب به حساب)
     */
    private String issueType;

    /**
     * مقدار درخواست به ریال
     */
    private Long amount;

    /**
     * uuid استفاده شده در سرویس انتقال پول داده ورزی سداد
     */
    private String paymentId;

}
