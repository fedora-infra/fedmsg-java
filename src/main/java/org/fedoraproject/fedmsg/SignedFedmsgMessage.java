package org.fedoraproject.fedmsg;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A fedmsg message after it has been signed with a valid certificate.
 *
 * @author Ricky Elrod
 * @version 1.0.0
 */
public final class SignedFedmsgMessage {
    private FedmsgMessage message;
    private String signature;
    private String certificate;

    public SignedFedmsgMessage(
        FedmsgMessage message,
        String signature,
        String certificate) {
        this.message = message;
        this.signature = signature;
        this.certificate = certificate;
    }

    public FedmsgMessage getMessage() {
        return this.message;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getCertificate() {
        return this.certificate;
    }

    /**
     * Converts this message into its JSON representation.
     */
    public ByteArrayOutputStream toJson() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter w = mapper.writer();
        w.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValue(os, this);
        os.close();
        return os;
    }
}
