package ir.co.sadad.investment.common.enumurations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = OriginationDeserializer.class)
public enum Origination {
    HAMBAM, BAMPAY, PORTAL, PWA
}
