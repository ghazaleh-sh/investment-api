package ir.co.sadad.investment.common.util;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Component
@AllArgsConstructor
public class TripleDES {
//    @Autowired
    private PrivateKey privateKey;

//    @Autowired
    private PublicKey publicKey;

    @SneakyThrows
    public String sign(String input) {

        /*byte[] id = new byte[]{0x30, 0x31, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01, 0x05, 0x00, 0x04, 0x20};
        byte[] derDigestInfo = new byte[id.length + hashBytes.length];
        System.arraycopy(id, 0, derDigestInfo, 0, id.length);
        System.arraycopy(hashBytes, 0, derDigestInfo, id.length, hashBytes.length);*/

        // SIGN HASH
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        return Base64.encodeBase64String(signatureBytes);
//        return signatureBytes;
    }


    public Boolean verify(String input, byte[] signatureBytes) throws Exception {

        //INITIALIZE SIGNATURE
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        //VERIFY SIGNATURE
        boolean verified = signature.verify(signatureBytes);

        //DISPLAY VERIFICATION
        System.out.println("VERIFIED  = " + verified);

        //RETURN SIGNATURE
        return verified;

    }

}