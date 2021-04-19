import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Signature {
    private final String secretKey;

    public Signature(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generate(String requestPath, String requestMethod, String requestBody, String timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
        String prehash = timestamp + requestMethod.toUpperCase() + requestPath + requestBody;
        byte[] secretKeyDecoded = Base64.getDecoder().decode(secretKey);
        SecretKeySpec keySpec = new SecretKeySpec(secretKeyDecoded, "HmacSHA256");
        Mac sha256HMAc = Mac.getInstance("HmacSHA256");
        sha256HMAc.init(keySpec);
        return Base64.getEncoder().encodeToString(sha256HMAc.doFinal(prehash.getBytes()));
    }
}
