package ir.co.sadad.investment.services;

import ir.co.sadad.investment.dto.RevokeReqDto;
import ir.co.sadad.investment.dto.RevokeResDto;

public interface RevokeService {

    RevokeResDto createInitialRevoke(RevokeReqDto revokeReqDto, String ssn, String otp);

    RevokeResDto createRevoke(RevokeReqDto revokeReqDto, String otp);

    RevokeResDto inquiryRevoke(String uuid);

    void cancelRevoke(String uuid, String fundId, String ssn, String otp);

}
