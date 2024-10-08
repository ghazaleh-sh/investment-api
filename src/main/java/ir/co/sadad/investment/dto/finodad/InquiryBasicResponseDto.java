package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

@Data
public class InquiryBasicResponseDto implements Serializable {
    /**
     * شناسه یکتا ارسال شده در ثبت درخواست
     */
    private String uuid;

    private String orderId;

    private String nationalCode;

    /**
     * وضعیت درخواست ارسال شده
     * ایجاد شده، ناموفق، لغو شده، تایید شده، یافت نشد.
     */
    private String state;

    /**
     * نوع درخواست
     */
    private String requestType;
    /**
     * شناسه پرداخت
     */
    private String paymentId;

    private String additionalData;

    /**
     * تاریخ و زمان ساخت
     * (YYYY/MM/DD HH:MM:SS)
     */
    private String createdAt;

    /**
     * تاریخ و زمان بروزرسانی
     * (YYYY/MM/DD HH:MM:SS)
     */
    private String updatedAt;

}
