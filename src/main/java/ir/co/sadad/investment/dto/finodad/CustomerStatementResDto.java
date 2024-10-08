package ir.co.sadad.investment.dto.finodad;

import lombok.Data;

import java.util.List;

/**
 * response of statement service of Finodad
 */
@Data
public class CustomerStatementResDto {

    private int totalRecords;

    private int currentPage;

    private int size;

    private List<StatementResDto> statements;
}
