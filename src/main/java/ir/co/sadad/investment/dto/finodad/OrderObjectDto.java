package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;

/**
 * آبجکت اطلاعات درخواست ها
 * به صورت لیست در پاسخ سرویس تاریخچه درخواست های مشتری استفاده میشود.
 */
@Data
public class OrderObjectDto implements Serializable {
    /**
     * شناسه درخواست
     */
    private String id;

    /**
     * نام صندوق سرمایه گذاری
     */
    private String fundName;

    /**
     * تعداد واحد
     */
    private String fundUnit;

    /**
     * (YYYY/MM/DD) تاریخ ایجاد درخواست به شمسی
     */
    private String creationTime;

    /**
     * نوع درخواست (صدور یا ابطال)
     */
    private String orderType;

    /**
     * مقداری ریالی درخواست
     */
    private String orderAmount;

    /**
     * وضعیت درخواست ها شامل: پیش نویس، انتظار تایید، تایید، حذف شده، رد مدیر، و رد سیستم.
     */
    private String status;

    /**
     * قیمت NAV هر واحد
     */
    private String unitPrice;

    private Long timestampCreationTime;

    /**
     * uuid that generate through sadad services
     */
    private String uuid;



}
