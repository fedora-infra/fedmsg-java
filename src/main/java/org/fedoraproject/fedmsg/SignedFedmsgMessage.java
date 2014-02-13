package org.fedoraproject.fedmsg;

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
}
