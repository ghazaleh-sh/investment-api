package ir.co.sadad.investment.common.enumurations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FundCreationState {
    CREATED("ایجاد شده"),
    CANCELED("لغو شده"),
    APPROVED("تاییده شده"),
    NOT_FOUND("یافت نشد"),
    UNSUCCESSFUL("ناموفق"),
    UUID_GENERATED("uuid تولید شده"),
    PAYMENT_TAN("کد تایید پرداخت"),
    PAYMENT_SUCCESSFUL("پرداخت موفق"),
    PAYMENT_UNSUCCESSFUL("پرداخت ناموفق"),
    PAYMENT_BY_JOB("پرداخت با جاب"),
    ISSUE_BY_JOB("صدور با جاب"),
    REVOKE_BY_JOB("ابطال با جاب");

    private final String title;

    public static FundCreationState getByTitle(String title) {
        for (FundCreationState e : values()) {
            if (e.getTitle().equals(title)) {
                return e;
            }
        }
        return null;
    }
}

