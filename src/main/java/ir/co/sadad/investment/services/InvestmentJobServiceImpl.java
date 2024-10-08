package ir.co.sadad.investment.services;

import ir.co.sadad.investment.common.enumurations.FundCreationState;
import ir.co.sadad.investment.common.enumurations.JobStatus;
import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.moneyTransfer.PaymentInquiryResDto;
import ir.co.sadad.investment.entities.InvestmentOrder;
import ir.co.sadad.investment.services.dao.InvestmentOrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvestmentJobServiceImpl implements InvestmentJobService {

    private InvestmentOrdersService orderService;
    private PaymentService paymentService;
    private IssueService issueService;
    private RevokeService revokeService;


    //Lazy: in order to delay the instantiation until it's actually needed.
    @Autowired
    public InvestmentJobServiceImpl(@Lazy PaymentService paymentService, @Lazy IssueService issueService,
                                    InvestmentOrdersService orderService, RevokeService revokeService) {
        this.paymentService = paymentService;
        this.issueService = issueService;
        this.orderService = orderService;
        this.revokeService = revokeService;
    }

    @Override
    public void issueRevokeJobService() {

        List<InvestmentOrder> savedOrders = orderService.filterOrdersForJob();

        if (!savedOrders.isEmpty()) {
            for (InvestmentOrder order : savedOrders) {
                orderService.updateOrderForJob(order, JobStatus.PROCESSING);

                if (order.getStatus().equals(FundCreationState.PAYMENT_BY_JOB)) {
                    PaymentInquiryResDto paymentRes = paymentService.paymentInquiry(order.getUuid());

                    issueService.createIssue(createIssueRequest(order), paymentRes.getInstructionIdentification());

                } else if (order.getStatus().equals(FundCreationState.ISSUE_BY_JOB)) {
                    issueService.inquiryIssue(order.getUuid());

                } else if (order.getStatus().equals(FundCreationState.REVOKE_BY_JOB)) {
                    revokeService.inquiryRevoke(order.getUuid());
                }
            }
        }
    }


    @Async("taskExecutor")
    public void executePaymentInquiryTask(IssueReqDto issueReqDto, String paymentId, long delayInSeconds) {

        // This scheduler is used for scheduling delayed tasks
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        log.info("Handling unknown payment Async task after 10 seconds....");

        scheduler.schedule(() ->
                asyncUnknownPaymentTask(issueReqDto, paymentId), delayInSeconds, TimeUnit.SECONDS);
    }

    /**
     * logic of unknown payment: it calls paymentInquiry after 10 sec and if successful, continues issue flow
     */
    private void asyncUnknownPaymentTask(IssueReqDto issueReqDto, String paymentId) {
        PaymentInquiryResDto paymentRes = paymentService.paymentInquiry(paymentId);
        issueService.createIssue(issueReqDto, paymentRes.getInstructionIdentification());
    }

    private IssueReqDto createIssueRequest(InvestmentOrder order) {
        IssueReqDto issueReqDto = new IssueReqDto();
        issueReqDto.setUuid(order.getUuid());
        issueReqDto.setAmount(order.getAmount());
        issueReqDto.setNationalCode(order.getFundUser().getNationalCode());
        issueReqDto.setFundId(order.getFundUser().getFundId());
        issueReqDto.setAccountId(order.getPayment().getFromAccount());
        return issueReqDto;
    }

}
