package ir.co.sadad.investment.mapper;

import ir.co.sadad.investment.dto.*;
import ir.co.sadad.investment.dto.finodad.*;
import ir.co.sadad.investment.entities.InvestmentFundUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerFundMapper {

    List<FundResponseDto> toFundListResponseList(List<FundListDetailResDto> funds);

    FundResponseDto toFundListResponse(FundListDetailResDto fund);

    @Mapping(
            expression = "java(ir.co.sadad.hambaam.persiandatetime.PersianUTC.toUTCDateOfBeginningOfDay(fundInfo.getCalcDate(),java.time.format. DateTimeFormatter.ofPattern(\"yyyy/MM/dd_HH:mm:ss\")).toString())",
            target = "calcDate"
    )
    FundInfoResponseDto toFundInfoResponse(FundDetailInfoResDto fundInfo);

    CustomerBalanceResponseDto toCustomerBalanceResponse(CustomerBalanceResDto balanceRes);

    InvestmentFundUser toEntity(FundBasicReqDto fundBasicReqDto);

    @Mapping(
            expression = "java(ir.co.sadad.hambaam.persiandatetime.PersianUTC.toUTCDateOfBeginningOfDay(infoRes.getBirthDate(),java.time.format.DateTimeFormatter.ofPattern(\"yyyy/MM/dd_HH:mm:ss\")).toString())",
            target = "birthDate"
    )
    CustomerInfoResponseDto toCustomerInfoResponse(CustomerInfoResDto infoRes);

    @Mapping(
            expression = "java(ir.co.sadad.hambaam.persiandatetime.PersianUTC.toUTCDateTime(response.getCreationTime(),java.time.format.DateTimeFormatter.ofPattern(\"yyyy/MM/dd HH:mm\")).toString())",
            target = "creationDate"
    )
    @Mapping(source = "status", target = "status", qualifiedByName = "statusConvert")
    @Mapping(source = "orderType", target = "orderType", qualifiedByName = "orderTypeConvert")
    OrderHistoryResponseDto toOrderHistoryResponse(OrderObjectDto response);

    List<OrderHistoryResponseDto> toOrderHistoryResponseList(List<OrderObjectDto> response);

    @Mapping(
            expression = "java(ir.co.sadad.hambaam.persiandatetime.PersianUTC.toUTCDateOfBeginningOfDay(response.getTransactionDate(),java.time.format.DateTimeFormatter.ofPattern(\"yyyy/MM/dd_HH:mm:ss\")).toString())",
            target = "transactionDate"
    )
    StatementResponseDto toCustomerStatementResponse(StatementResDto response);

    List<StatementResponseDto> toCustomerStatementResponseList(List<StatementResDto> response);



    CustomerInquiryResponseDto toCustomerInquiryResponse(CustomerInquiryResDto inquiry);

    @Named("statusConvert")
    default String orderHistoryStatusConvert(String status) {
        return switch (status) {
            case "پيش نويس", "انتظار تایید" -> "PENDING";
            case "تایید" , "تاييد" -> "CONFIRMED";
            case "حذف شده" -> "DELETED";
            case "رد مدیر", "رد سیستم" -> "REJECTED";
            default -> "";
        };
    }

    @Named("orderTypeConvert")
    default String orderTypeConvert(String orderType) {
        return switch (orderType) {
            case "صدور" -> "ISSUE";
            case "ابطال" -> "REVOKE";
            default -> "";
        };
    }
}
