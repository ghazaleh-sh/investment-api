package ir.co.sadad.investment.dto.eb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EbAccountResponseDto {
    @JsonProperty("Id")
    public String id;
    @JsonProperty("Cif")
    public String cif;
    @JsonProperty("BranchCode")
    public int branchCode;
    @JsonProperty("OpenDate")
    public String openDate;
    @JsonProperty("SicCode")
    public String sicCode;
    @JsonProperty("BackupNumber")
    public String backupNumber;
    @JsonProperty("FreezAmount")
    public String freezAmount;
    @JsonProperty("FreezBranchCode")
    public String freezBranchCode;
    @JsonProperty("ProfitAccountNumber")
    public String profitAccountNumber;
    @JsonProperty("FirstName")
    public String firstName;
    @JsonProperty("LastName")
    public String lastName;
    @JsonProperty("IBAN")
    public String iBAN;
    @JsonProperty("Type")
    public String type;
    @JsonProperty("SubType")
    public String subType;
    @JsonProperty("State")
    public String state;
    @JsonProperty("FreezDate")
    public String freezDate;
    @JsonProperty("Rate")
    public int rate;
    @JsonProperty("AvailableBalance")
    public double availableBalance;
    @JsonProperty("UsableBalance")
    public double usableBalance;
    @JsonProperty("CurrentBalance")
    public double currentBalance;
    @JsonProperty("LastTransactionDate")
    public Object lastTransactionDate;
    @JsonProperty("CustomerType")
    public String customerType;
    @JsonProperty("CloseDate")
    public String closeDate;
}
