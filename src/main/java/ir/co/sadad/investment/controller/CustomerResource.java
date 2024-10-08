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
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerFundService customerFundService;
    private final PaymentService paymentService;


}
