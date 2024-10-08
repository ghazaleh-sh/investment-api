package ir.co.sadad.investment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.co.sadad.investment.dto.RevokeReqDto;
import ir.co.sadad.investment.dto.RevokeResDto;
import ir.co.sadad.investment.services.InvestmentJobService;
import ir.co.sadad.investment.services.RevokeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ir.co.sadad.investment.common.Constants.SSN;

@RestController
@RequestMapping(path = "${v1API}/revoke")
@Tag(description = "سرویس های ابطال", name = "Revoke services")
@RequiredArgsConstructor
public class RevokeResource {
    private final RevokeService revokeService;
    private final InvestmentJobService jobService;

    @Operation(summary = "سرویس درخواست ابطال")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = RevokeResDto.class)))
    @PostMapping("/create")
    public ResponseEntity<RevokeResDto> createRevoke(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                     @RequestHeader(SSN) String ssn,
                                                     @Valid @RequestBody RevokeReqDto revokeReqDto,
                                                     @RequestHeader(name = "otp", required = false) String otp) {
        return new ResponseEntity<>(revokeService.createInitialRevoke(revokeReqDto, ssn, otp), HttpStatus.OK);
    }

    @Operation(summary = "سرویس لغو ابطال")
    @ApiResponse(responseCode = "204")
    @PostMapping("/cancel")
    public ResponseEntity<HttpStatus> cancelRevoke(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authToken,
                                                   @RequestHeader(SSN) String ssn,
                                                   @RequestHeader(name = "otp", required = false) String otp,
                                                   @RequestParam(name = "uuid") String uuid,
                                                   @RequestParam(name = "fundId") String fundId) {
        revokeService.cancelRevoke(uuid, fundId, ssn, otp);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
