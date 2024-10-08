package ir.co.sadad.investment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.co.sadad.investment.common.enumurations.PaymentHistoryStatus;
import ir.co.sadad.investment.common.enumurations.SortDirection;
import ir.co.sadad.investment.dto.*;
import ir.co.sadad.investment.services.CustomerFundService;
import ir.co.sadad.investment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ir.co.sadad.investment.common.Constants.OTP;
import static ir.co.sadad.investment.common.Constants.SSN;

@RestController
@RequestMapping(path = "${v1API}/customer")
@Tag(description = "سرویس های مشتری", name = "Customer services")
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerFundService customerFundService;
    private final PaymentService paymentService;


    @Operation(summary = "سرویس دارایی های مشتری ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerBalanceResponseDto.class)))
    @GetMapping("/balance/{fundId}")
    public ResponseEntity<CustomerBalanceResponseDto> getCustomerBalance(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                                         @RequestHeader(SSN) String ssn,
                                                                         @PathVariable("fundId") Integer fundId) {
        return new ResponseEntity<>(customerFundService.getCustomerBalance(ssn, fundId), HttpStatus.OK);
    }


    @Operation(summary = "سرویس مشخصات مشتری ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerInfoResponseDto.class)))
    @GetMapping("/info/{fundId}")
    public ResponseEntity<CustomerInfoResponseDto> customerInfo(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                                @RequestHeader(SSN) String ssn,
                                                                @PathVariable("fundId") Integer fundId) {
        return new ResponseEntity<>(customerFundService.getCustomerInfo(ssn, fundId), HttpStatus.OK);
    }

    @Operation(summary = "سرویس استعلام مشتری ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CustomerInquiryResponseDto.class)))
    @GetMapping("/inquiry/{fundId}")
    public ResponseEntity<CustomerInquiryResponseDto> customerInquiry(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                                      @RequestHeader(SSN) String ssn,
                                                                      @PathVariable("fundId") Integer fundId) {
        return new ResponseEntity<>(customerFundService.getCustomerInquiry(ssn, fundId), HttpStatus.OK);
    }


    @Operation(summary = "سرویس ثبت نام مشتری ")
    @ApiResponse(responseCode = "204")
    @PostMapping("/register/{fundId}")
    public ResponseEntity<HttpStatus> register(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                               @RequestHeader(SSN) String ssn,
                                               @RequestHeader(value = OTP, required = false) String otpCode,
                                               @PathVariable("fundId") Integer fundId) {
        customerFundService.register(ssn, fundId, otpCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "سرویس تاریخچه درخواست ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = OrderHistoryResponseDto.class)))
    @GetMapping("/order-history/{fundId}")
    public ResponseEntity<List<OrderHistoryResponseDto>> info(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                              @RequestHeader(SSN) String ssn,
                                                              @PathVariable("fundId") Integer fundId,
                                                              @RequestParam(name = "page_number") Integer pageNumber,
                                                              @RequestParam(name = "page_size") Integer pageSize) {
        return new ResponseEntity<>(customerFundService.getOrderHistory(ssn, fundId, pageNumber, pageSize), HttpStatus.OK);
    }

    @Operation(summary = "سرویس تاریخچه صورتحساب ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = StatementResponseDto.class)))
    @GetMapping("/statement/{fundId}")
    public ResponseEntity<List<StatementResponseDto>> statement(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                              @RequestHeader(SSN) String ssn,
                                                              @PathVariable("fundId") Integer fundId) {
        return new ResponseEntity<>(customerFundService.getCustomerStatement(ssn, fundId), HttpStatus.OK);
    }


    @Operation(summary = "سرویس تاریخچه واریز ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PaymentHistoryResDto.class)))
    @GetMapping("/payment-history/{fundId}")
    public ResponseEntity<List<PaymentHistoryResDto>> paymentHistory(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                                     @RequestHeader(SSN) String ssn,
                                                                     @PathVariable("fundId") Integer fundId,
                                                                     @RequestParam(name = "page_number") Integer pageNumber,
                                                                     @RequestParam(name = "page_size") Integer pageSize,
                                                                     @RequestParam(name = "sort_type", required = false,defaultValue = "DESC") SortDirection sortType,
                                                                     @RequestParam(name = "amount_gt", required = false) Long amountFrom,
                                                                     @RequestParam(name = "amount_lt", required = false) Long amountTo,
                                                                     @RequestParam(name = "date_gt", required = false) String dateFrom,
                                                                     @RequestParam(name = "date_lt", required = false) String dateTo,
                                                                     @RequestParam(name = "status", required = false) PaymentHistoryStatus status
    ) {
        return new ResponseEntity<>(paymentService.getPaymentHistory(pageNumber,
                pageSize,
                sortType,
                PaymentHistorySearchFilterDto.builder()
                        .amountFrom(amountFrom)
                        .amountTo(amountTo)
                        .dateTo(dateFrom)
                        .dateFrom(dateTo)
                        .fundId(fundId)
                        .ssn(ssn)
                        .status(status)
                        .build()), HttpStatus.OK);
    }


}
