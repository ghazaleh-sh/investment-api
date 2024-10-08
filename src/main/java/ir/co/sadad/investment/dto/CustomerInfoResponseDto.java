package ir.co.sadad.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "مشخصات مشتری")
@Data
public class CustomerInfoResponseDto {

    @Schema(title = "شماره حساب مشتری")
    private String accountNumber;
    @Schema(title = "نام بانک")
    private String bankName;
    @Schema(title = "شماره شبا مشتری")
    private String iban;
    @Schema(title = "نام مشتری")
    private String firstName;
    @Schema(title = "نام خانوادگی مشتری")
    private String lastName;
    @Schema(title = "نام پدر مشتری")
    private String fatherName;
    @Schema(title = "تاریخ تولد مشتری ")
    private String birthDate;
    @Schema(title = "کد ملی مشتری ")
    private String nationalCode;
    @Schema(title = "شماره تلفن همراه مشتری")
    private String mobileNumber;
    @Schema(title = "ایمیل مشتری")
    private String email;

}
