package ir.co.sadad.investment.services.dao;

import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.dto.finodad.FundBasicReqDto;
import ir.co.sadad.investment.entities.InvestmentFundUser;
import ir.co.sadad.investment.mapper.CustomerFundMapper;
import ir.co.sadad.investment.repositories.InvestmentFundUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * service for manage fundCustomer entity
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class InvestmentFundUserService {

    private final InvestmentFundUserRepository fundUserRepository;
    private final CustomerFundMapper mapper;

    /**
     * get list of fundUser based on ssn of customer
     *
     * @param nationalCode ssn of customer
     * @return list of funds of user <pre>
     *     if there is no fund for user this method will return null
     * </pre>
     */
    public List<InvestmentFundUser> getBy(String nationalCode) {
        return fundUserRepository.findByNationalCode(nationalCode).orElse(null);
    }

    /**
     * get customerFund based oon fundId and customer ssn
     *
     * @param nationalCode ssn of customer
     * @param fundId       id of fund
     * @return InvestmentFundUser
     * @throws InvestmentException throws exception when notFound any user
     */
    public InvestmentFundUser getBy(String nationalCode, String fundId) {
        return fundUserRepository.findByNationalCodeAndFundId(nationalCode,
                fundId).orElseThrow(() -> new InvestmentException("BUSINESS_CUSTOMER_FUND_NOT_FOUND", HttpStatus.BAD_REQUEST));
    }

    /**
     * create a record for user
     *
     * @param fundBasicReqDto request for create record , fundId and ssn of customer
     */
    public void createFundUser(FundBasicReqDto fundBasicReqDto) {
        InvestmentFundUser fundUserToSave = mapper.toEntity(fundBasicReqDto);
        fundUserRepository.saveAndFlush(fundUserToSave);
    }

    /**
     * check for existence of customer fund in DB
     *
     * @param fundBasicReqDto request for search in db
     * @return there is record or not
     */
    public Boolean existsCustomerFund(FundBasicReqDto fundBasicReqDto) {
        return fundUserRepository.existsByNationalCodeAndFundId(fundBasicReqDto.getNationalCode(),
                fundBasicReqDto.getFundId());
    }
}
