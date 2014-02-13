package org.fedoraproject.fedmsg;

/**
 * A fedmsg message after it has been signed with a valid certificate.
 *
 * @author Ricky Elrod
 */
public final class SignedFedmsgMessage extends FedmsgMessage {
    private String signature;
    private String certificate;

    public SignedFedmsgMessage(
        FedmsgMessage msg,
        String signature,
        String certificate) {
        super(msg);
        this.signature = signature;
        this.certificate = certificate;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getCertificate() {
        return this.certificate;
    }
}
