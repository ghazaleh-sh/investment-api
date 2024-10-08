package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreationBaseRequestDto implements Serializable {
    /**
     * شناسه یکتا ایجاد شده از سرویس ایجاد شناسه یکتا
     */
    private String uuid;
    /**
     * شناسه که از خروجی سرویس لیست صندوق های مجاز
     */
    private String fundId;
    /**
     * کد ملی مشخری صندوق
     */
    private String nationalCode;
    /**
     * توضیحات اختیاری
     */
    private String additionalData;
    /**
     * امضاء با استفاده از پارامترهای زیر باید ایجاد می شود
     * amount + "|" + nationalCode + "|" + fundId + "|" + uuid
     */
    private String signature;
}
