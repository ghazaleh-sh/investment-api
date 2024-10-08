package ir.co.sadad.investment.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.co.sadad.investment.dto.finodad.ThirdPartyServicesResponse;

public class ConverterHelper {

    public static String convertResponseToJson(ThirdPartyServicesResponse data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (data != null)
                return objectMapper.writeValueAsString(data);
            else return null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}

