package ir.co.sadad.investment.services.dao;

import ir.co.sadad.hambaam.persiandatetime.PersianUTC;
import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.FundRequestType;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import ir.co.sadad.investment.common.exception.InvestmentException;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.IssueResDto;
import ir.co.sadad.investment.dto.RevokeReqDto;
import ir.co.sadad.investment.dto.RevokeResDto;
import ir.co.sadad.investment.dto.finodad.FundBasicReqDto;
import ir.co.sadad.investment.entities.InvestmentFundUser;
import ir.co.sadad.investment.entities.InvestmentOrder;
import ir.co.sadad.investment.entities.InvestmentPayment;
import ir.co.sadad.investment.mapper.OrderMapper;
import ir.co.sadad.investment.repositories.InvestmentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * DAO for manage order
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class InvestmentOrdersService {

    private final InvestmentOrderRepository ordersRepository;
    private final InvestmentFundUserService investmentFundUserService;
    private final OrderMapper mapper;

    /**
     * get order by info of user
     *
     * @param fundUser info of user
     * @return list of orders for a user
     */
    List<InvestmentOrder> getBy(InvestmentFundUser fundUser) {
        return ordersRepository.findByFundUser(fundUser).orElseGet(Collections::emptyList);
    }

    /**
     * get order based on ssn and fundId
     *
     * @param fundReqDto req contains ssn and fundId of user
     * @return list of orders for a user
     */
    public List<InvestmentOrder> getBy(FundBasicReqDto fundReqDto) {
        InvestmentFundUser savedFundUser = investmentFundUserService.getBy(fundReqDto.getNationalCode(), fundReqDto.getFundId());
        return getBy(savedFundUser);
    }

    /**
     * create an order for Issue
     * <pre>
     *     this method will have UUID that generated from Finodad services
     * </pre>
     *
     * @param issueReqDto req for create issue order
     */
    public void createIssueOrder(IssueReqDto issueReqDto) {
        InvestmentFundUser savedFundUser = investmentFundUserService.getBy(issueReqDto.getNationalCode(), issueReqDto.getFundId());
        InvestmentOrder orderToSave = mapper.issueReqToEntity(issueReqDto);
        orderToSave.setFundUser(savedFundUser);
        orderToSave.setRequestType(FundRequestType.ISSUE);
        orderToSave.setStatus(FundCreationState.UUID_GENERATED);
        ordersRepository.saveAndFlush(orderToSave);
    }

    /**
     * add payment to existing order
     *
     * @param uuid    uuid for find order
     * @param payment payment info
     */
    public void updateIssueOrderWithPaymentEntity(String uuid, InvestmentPayment payment) {
        InvestmentOrder savedOrder = getBy(uuid);
        savedOrder.setStatus(FundCreationState.PAYMENT_TAN);
        savedOrder.setPayment(payment);
        ordersRepository.saveAndFlush(savedOrder);
    }

    /**
     * add orderId to existing order
     *
     * @param uuid    generated uuid for find order
     * @param orderId orderId
     * @return updated Issue order
     */
    public IssueResDto updateIssueOrderWithOrderId(String uuid, String orderId, JobStatus jobStatus) {
        InvestmentOrder savedOrder = getBy(uuid);
        savedOrder.setStatus(FundCreationState.CREATED);
        savedOrder.setOrderId(orderId);
        savedOrder.setCreatedDate(PersianUTC.currentUTC().toString());
        if (savedOrder.getJobStatus() != null)
            updateOrderForJob(savedOrder, jobStatus);
        else
            ordersRepository.saveAndFlush(savedOrder);
        return mapper.toIssueResDto(savedOrder);
    }

    /**
     * update status of an order
     *
     * @param uuid   generated uuid for find order
     * @param status new status
     */
    public void updateOrderStatus(String uuid, FundCreationState status, JobStatus jobStatus) {
        InvestmentOrder savedOrder = getBy(uuid);
        savedOrder.setStatus(status);
        if (savedOrder.getJobStatus() != null)
            updateOrderForJob(savedOrder, jobStatus);
        else
            ordersRepository.saveAndFlush(savedOrder);
    }

    /**
     * update status of an order based on payment
     *
     * @param savedPayment update issue status based on payment which is saved into order
     * @param status       new status
     */
    public void updateIssueOrderPaymentStatus(InvestmentPayment savedPayment, FundCreationState status, JobStatus jobStatus) {
        try {
            InvestmentOrder savedOrder = ordersRepository.findByPayment(savedPayment)
                    .orElseThrow(() -> new InvestmentException("PAYMENT_NOT_SAVED_INFO_ORDER", HttpStatus.INTERNAL_SERVER_ERROR));
            updateOrderStatus(savedOrder.getUuid(), status, jobStatus);
        } catch (InvestmentException e) {
            log.error("payment was not saved into order in the first step ----->{}", e.getMessage());
        }
    }

    /**
     * get order based on uuid
     *
     * @param uuid uuid that generated for an order
     * @return saved order
     * @throws ir.co.sadad.investment.common.exception.InvestmentException throws exceptions when not found order
     */
    public InvestmentOrder getBy(String uuid) {
        return ordersRepository.findByUuid(uuid)
                .orElseThrow(() -> new InvestmentException("BUSINESS_ORDER_NOT_FOUND", HttpStatus.BAD_REQUEST));
    }

    /**
     * for filtering orders based on fundCreationStatus = issue_by_job or payment_by_job and jobStatus = null or not SUCCEEDED
     *
     * @return list of investmentOrder to be considered in job operation
     */
    public List<InvestmentOrder> filterOrdersForJob() {
        return ordersRepository.findAll(InvestmentOrderRepository.orderStatusFilter());
    }

    /**
     * get order based on search filters that is given by user
     *
     * @param specification search filters
     * @param pageable      pagination of order
     * @return listed orders for user
     */
    public Page<InvestmentOrder> getPaginationList(Specification<InvestmentOrder> specification, Pageable pageable) {
        return ordersRepository.findAll(specification, pageable);
    }

    /**
     * change job status in the beginning, ending, and between job operation
     *
     * @param jobStatus
     */
    public void updateOrderForJob(InvestmentOrder savedOrder, JobStatus jobStatus) {
//        InvestmentOrder savedOrder = (uuid != null) ? getBy(uuid) : currentOrder;
        savedOrder.setJobStatus(jobStatus);
        savedOrder.setJobDuration(savedOrder.getJobDuration());//TODO: how to fill it??
        savedOrder.setResponseTime(PersianUTC.currentUTC().toString());
        ordersRepository.saveAndFlush(savedOrder);
    }

    /*-----------------revoke crud------------------*/
    public void createRevokeOrder(RevokeReqDto revokeReqDto) {
        InvestmentFundUser savedFundUser = investmentFundUserService.getBy(revokeReqDto.getNationalCode(), revokeReqDto.getFundId());
        InvestmentOrder orderToSave = mapper.revokeReqToEntity(revokeReqDto);
        orderToSave.setFundUser(savedFundUser);
        orderToSave.setRequestType(FundRequestType.REVOKE);
        orderToSave.setStatus(FundCreationState.UUID_GENERATED);
        ordersRepository.saveAndFlush(orderToSave);
    }

    /**
     * add orderId to existing order
     *
     * @param uuid    generated uuid for find order
     * @param orderId orderId
     * @return updated revoke order
     */
    public RevokeResDto updateRevokeOrderWithOrderId(String uuid, String orderId, JobStatus jobStatus) {
        InvestmentOrder savedOrder = getBy(uuid);
        savedOrder.setStatus(FundCreationState.CREATED);
        savedOrder.setOrderId(orderId);
        savedOrder.setCreatedDate(PersianUTC.currentUTC().toString());
        if (savedOrder.getJobStatus() != null)
            updateOrderForJob(savedOrder, jobStatus);
        else
            ordersRepository.saveAndFlush(savedOrder);
        return mapper.toRevokeResDto(savedOrder);
    }
}
