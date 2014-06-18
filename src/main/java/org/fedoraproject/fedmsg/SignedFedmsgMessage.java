package org.fedoraproject.fedmsg;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A fedmsg message after it has been signed with a valid certificate.
 *
 * @author Ricky Elrod
 * @version 1.0.0
 */
@JsonPropertyOrder(alphabetic=true)
public final class SignedFedmsgMessage extends FedmsgMessage {
    final private String signature;
    final private String certificate;

    public SignedFedmsgMessage(
        FedmsgMessage message,
        String signature,
        String certificate) {
        super(message);
        this.signature = signature;
        this.certificate = certificate;
    }

    public final String getSignature() {
        return this.signature;
    }

    public final String getCertificate() {
        return this.certificate;
    }
}
