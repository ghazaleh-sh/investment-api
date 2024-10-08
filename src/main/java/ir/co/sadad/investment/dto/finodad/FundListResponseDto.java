package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FundListResponseDto implements Serializable {

    private List<FundListDetailResDto> funds;

}
