package ir.co.sadad.investment.mapper;

import ir.co.sadad.investment.dto.moneyTransfer.SendClientReqDto;
import ir.co.sadad.investment.entities.InvestmentPayment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PaymentMapper {

    InvestmentPayment toEntity(SendClientReqDto clientReqDto);

}
