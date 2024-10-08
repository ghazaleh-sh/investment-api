package ir.co.sadad.investment.services;

import ir.co.sadad.investment.dto.IssueReqDto;

public interface InvestmentJobService {

    void issueRevokeJobService();

    /**
     * Executes a task after a specified delay using a ScheduledExecutorService.
     * The task is then run on a custom thread pool -> (taskExecutor).
     *
     * @param delayInSeconds the seconds we want to run in it.
     * @Async indicating that it should run on a separate thread, without blocking the caller method.
     * using "taskExecutor" indicates the task is submitted to the taskExecutor bean for execution,
     * allowing you to benefit from the customized thread pool
     */
    void executePaymentInquiryTask(IssueReqDto issueReqDto, String paymentId, long delayInSeconds);
}
