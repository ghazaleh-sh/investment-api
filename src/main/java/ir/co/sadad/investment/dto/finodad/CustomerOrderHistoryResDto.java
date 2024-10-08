package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * پاسخ سرویس تاریخچه درخواست های مشتری
 */
@Data
public class CustomerOrderHistoryResDto implements Serializable {
    /**
     * تعداد کل درخواست های مشتری
     */
    private Integer totalElements;

    /**
     * شماره صفحه
     */
    private Integer number;

    /**
     * اندازه صفحه
     */
    private Integer size;

    /**
     * که اطلاعات درخواست ها را بر می گرداند order لیستی از نوع
     */
    private List<OrderObjectDto> orders;

}
