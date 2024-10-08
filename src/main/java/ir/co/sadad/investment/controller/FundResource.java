package ir.co.sadad.investment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.co.sadad.investment.dto.FundInfoResponseDto;
import ir.co.sadad.investment.dto.FundResponseDto;
import ir.co.sadad.investment.services.CustomerFundService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ir.co.sadad.investment.common.Constants.SSN;

@RestController
@RequestMapping(path = "${v1API}/fund")
@Tag(description = "سرویس های صندوق", name = "Funds services")
@RequiredArgsConstructor
public class FundResource {
    private static final Logger log = LoggerFactory.getLogger(FundResource.class);
    private final CustomerFundService customerFundService;

    @Operation(summary = "سرویس دریافت لیست صندوق ها ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = FundResponseDto.class)))
    @GetMapping("/list")
    public ResponseEntity<List<FundResponseDto>> add(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                     @RequestHeader(name = "X-Gateway-Path",required = false) String baseUrl,
                                                     @RequestHeader(SSN) String ssn) {

        return new ResponseEntity<>(customerFundService.getCustomerFund(ssn, baseUrl), HttpStatus.OK);
    }

    @Operation(summary = "سرویس مشخصات صندوق ")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = FundInfoResponseDto.class)))
    @GetMapping("/info/{fundId}")
    public ResponseEntity<FundInfoResponseDto> info(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                    @PathVariable("fundId") Integer fundId) {
        return new ResponseEntity<>(customerFundService.getFundInfo(fundId), HttpStatus.OK);
    }
}
