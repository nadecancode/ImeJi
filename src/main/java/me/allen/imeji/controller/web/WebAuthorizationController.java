package me.allen.imeji.controller.web;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.allen.imeji.util.id.IdentifierGenerator;
import org.rapidoid.http.Req;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/*
    Enlightened from AdvancedLicense
    Session System with randomized tokens
    More like OAuth but it's actually a little bit different
 */
@RequiredArgsConstructor
public class WebAuthorizationController {

    private final Map<Long, String> tokenSessions = Maps.newConcurrentMap();

    private final String authorizationKey;

    private final String TOKEN_ID_HEADER = "ImeJi|TokenID";
    private final String CONNECTION_ID_HEADER = "ImeJi|ConnectionID";

    public boolean hasSession(Req req) {
        if (!req.token().containsKey("ImeJi|TokenID") || !req.token().containsKey("ImeJi|ConnectionID") || !tokenSessions.containsKey(req.connectionId()))
            return false;

        String authorizeKey = toBinary(this.authorizationKey);

        String tokenId = req.token(TOKEN_ID_HEADER);
        String connectionId = req.token(CONNECTION_ID_HEADER);

        String randomToken = xor(tokenId, authorizeKey);
        String connectionTokenBinary = xor(String.valueOf(connectionId), randomToken);
        String connectionToken = fromBinary(connectionTokenBinary);

        return this.tokenSessions.get(req.connectionId()) != null && this.tokenSessions.get(req.connectionId()).equals(connectionToken);
    }

    public void setSession(Req req) {
        if (hasSession(req)) return;
        String randomKey = IdentifierGenerator.getId();
        String randomToken = toBinary(UUID.randomUUID().toString());
        String authorizeToken = toBinary(this.authorizationKey);
        req.token().put(TOKEN_ID_HEADER, xor(randomToken, authorizeToken));
        req.token().put(CONNECTION_ID_HEADER, xor(randomToken, toBinary(randomKey)));
        this.tokenSessions.put(req.connectionId(), randomKey);
    }

    public void removeSession(Req req) {
        if (!hasSession(req)) return;
        req.token().remove(TOKEN_ID_HEADER);
        req.token().remove(CONNECTION_ID_HEADER);
        this.tokenSessions.remove(req.connectionId());
    }

    /*
        xor String - https://stackoverflow.com/questions/5126616/xor-operation-with-two-strings-in-java
     */
    public String xor(String s, String key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            sb.append((char) (s.charAt(i) ^ key.charAt(i % key.length())));
        return sb.toString();
    }

    private String toBinary(String s) {
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    /*
        Faster way for fromBinary - https://stackoverflow.com/questions/4211705/binary-to-text-in-java
     */
    private String fromBinary(String binary) {
        return Arrays.stream(binary.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
                .parallel()
                .map(eightBits -> (char) Integer.parseInt(eightBits, 2))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }
}
