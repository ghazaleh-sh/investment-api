package ir.co.sadad.investment.services;

import ir.co.sadad.investment.dto.IssueReqDto;
import ir.co.sadad.investment.dto.IssueResDto;

public interface IssueService {

    /**
     * Firstly, call generateUuid. if it is not successful, flow is finished
     * Then, do payment based on bankAccountNumber received from fundList cache. if payment is not successful flow is finished
     * Then, call {createIssue} method
     *
     * @param issueReqDto
     * @return
     */
    IssueResDto createInitialIssue(IssueReqDto issueReqDto, String customerToken, String ssn, String authorizationCode);

    /**
     * call issue service of Finodad
     * if it is not successful based on some error-codes of finodad, try issueInquiry
     *
     * @param issueReqDto
     * @param instructionIdentification
     * @return
     */
    IssueResDto createIssue(IssueReqDto issueReqDto, String instructionIdentification);

    /**
     * to inquiry issue status
     *
     * @param uuid to retrieve inquiry result of issue
     * @return
     */
    IssueResDto inquiryIssue(String uuid);

    /**
     * to cancel issue request until is not completed
     *
     * @param uuid
     * @param fundId
     * @param ssn
     * @param otp
     */
    void cancelIssue(String uuid, String fundId, String ssn, String otp);
}
