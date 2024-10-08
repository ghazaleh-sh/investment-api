package ir.co.sadad.investment.common.enumurations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Objects;

public class OriginationDeserializer extends JsonDeserializer<Origination> {

    public Origination deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException {
        ObjectCodec objectCodec = p.getCodec();
        JsonNode node = objectCodec.readTree(p);
        return (node == null || Objects.equals(node.asText(), "")) ? Origination.HAMBAM : Origination.valueOf(node.textValue());
    }
}