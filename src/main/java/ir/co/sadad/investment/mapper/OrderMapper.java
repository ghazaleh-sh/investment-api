package ir.co.sadad.investment.mapper;

import ir.co.sadad.investment.dto.*;
import ir.co.sadad.investment.dto.finodad.IssueCreationReqDto;
import ir.co.sadad.investment.dto.finodad.IssueCreationResDto;
import ir.co.sadad.investment.dto.finodad.IssueInquiryResDto;
import ir.co.sadad.investment.dto.finodad.RevokeCreationReqDto;
import ir.co.sadad.investment.entities.InvestmentOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * mapper for orders services
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrderMapper {

    InvestmentOrder issueReqToEntity(IssueReqDto issueReqDto);

    IssueReqDto toIssueReqDto(InvestmentOrder investmentOrder);

    //    @Mapping(constant = "0", target = "paymentType")
    IssueCreationReqDto toIssueClient(IssueReqDto issueReqDto);

    IssueResDto fromIssueClient(IssueCreationResDto issueCreationResDto);

    IssueResDto toIssueResDto(InvestmentOrder investmentOrder);

    @Mapping(source = "state", target = "state", ignore = true)
    @Mapping(source = "requestType", target = "requestType", ignore = true)
    InquiryResDto toInquiryResDto(IssueInquiryResDto issueInquiryResDto);

    InvestmentOrder revokeReqToEntity(RevokeReqDto revokeReqDto);

    RevokeCreationReqDto toRevokeClient(RevokeReqDto revokeReqDto);

    RevokeResDto toRevokeResDto(InvestmentOrder investmentOrder);

}
