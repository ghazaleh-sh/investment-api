package ir.co.sadad.investment.common.enumurations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FundRequestType {

    ISSUE("ثبت درخواست صدور"),
    REVOKE("ثبت درخواست ابطال");

    private final String title;

    public static FundRequestType getByTitle(String title) {
        for (FundRequestType e : values()) {
            if (e.getTitle().equals(title)) {
                return e;
            }
        }
        return null;
    }

}
