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
@RequestMapping(path = "${v1API}/issue")
@Tag(description = "سرویس های صدور", name = "issue services")
@RequiredArgsConstructor
public class IssueResource {
    private final IssueService issueService;
    private final InvestmentJobService jobService;


    @Operation(summary = "سرویس درخواست صدور")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = IssueResDto.class)))
    @PostMapping("/create")
    public ResponseEntity<IssueResDto> createIssue(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                   @RequestHeader(SSN) String ssn,
                                                   @Valid @RequestBody IssueReqDto issueReqDto,
                                                   @RequestHeader(name = "otp", required = false) String authorizationCode) {
        return new ResponseEntity<>(issueService.createInitialIssue(issueReqDto, authToken, ssn, authorizationCode), HttpStatus.OK);
    }

    @Operation(summary = "سرویس لغو صدور")
    @ApiResponse(responseCode = "204")
    @PostMapping("/cancel")
    public ResponseEntity<HttpStatus> cancelIssue(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                  @RequestHeader(SSN) String ssn,
                                                  @RequestHeader(name = "otp", required = false) String otp,
                                                  @RequestParam(name = "uuid") String uuid,
                                                  @RequestParam(name = "fundId") String fundId) {
        issueService.cancelIssue(uuid, fundId, ssn, otp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "صدور/ابطال از طریق جاب")
    @GetMapping("/job")
    public ResponseEntity<Object> investmentByJob() {
        jobService.issueRevokeJobService();
        return ResponseEntity.ok().build();
    }
}
