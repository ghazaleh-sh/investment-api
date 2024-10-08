package ir.co.sadad.investment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.IssueResDto;
import ir.co.sadad.investment.services.InvestmentJobService;
import ir.co.sadad.investment.services.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ir.co.sadad.investment.common.Constants.SSN;

@RestController
@RequiredArgsConstructor
public class IssueResource {
    
}
