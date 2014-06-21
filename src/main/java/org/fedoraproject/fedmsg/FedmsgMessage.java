package org.fedoraproject.fedmsg;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Date;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import fj.data.Either;
import fj.data.IO;

/**
 * An <b>unsigned</b> fedmsg message. You likely never want to send this over
 * the bus (unless you are debugging/developing). Instead you want to send
 * {@link SignedFedmsgMessage} which includes the signature and certificate.
 *
 * This class is immutable, although calls to {@link sign(File, File)} do
 * side effect. This is wrapped in a FunctionalJava IO monad, but this does not
 * imply purity. :'(
 *
 * You can convert this to a {@link SignedFedmsgMessage} by calling
 * {@link sign(File, File)}. Keep in mind that this is wrapped in
 * {@link fj.data.IO}.
 *
 * @author Ricky Elrod
 * @version 2.0.0
 * @see SignedFedmsgMessage
 */
@JsonPropertyOrder(alphabetic=true)
public class FedmsgMessage {
    final private HashMap<String, Object> message;
    final private String topic;
    final private long timestamp;
    final private long i; // What is this?

    public FedmsgMessage(
        final HashMap<String, Object> message,
        final String topic,
        final long timestamp,
        final long i) {
        this.message = message;
        this.topic = topic;
        this.timestamp = timestamp;
        this.i = i;
    }

    protected FedmsgMessage(FedmsgMessage orig) {
        this.message = orig.getMessage();
        this.topic = orig.getTopic();
        this.timestamp = orig.getTimestamp().getTime();
        this.i = orig.getI();
    }

    // ew, Object. :-(
    @JsonProperty("msg")
    public final HashMap<String, Object> getMessage() {
        return this.message;
    }

    public final String getTopic() {
        return this.topic;
    }

    public final Date getTimestamp() {
        return new Date(this.timestamp);
    }

    public final long getI() {
        return this.i;
    }

    /**
     * Converts this message into its JSON representation.
     */
    public final ByteArrayOutputStream toJson() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter w = mapper.writer();
        w.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValue(os, this);
        os.close();
        return os;
    }

    /**
     * Signs a message.
     */
    public final IO<Either<Exception, SignedFedmsgMessage>> sign(final File cert, final File key)
        throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException {

        return new IO<Either<Exception, SignedFedmsgMessage>>() {
            public Either<Exception, SignedFedmsgMessage> run() {
                Security.addProvider(new BouncyCastleProvider());

                try {
                    PEMParser certParser = new PEMParser(new FileReader(cert));
                    ByteArrayOutputStream certOS = new ByteArrayOutputStream();
                    PEMWriter certWriter = new PEMWriter(new OutputStreamWriter(certOS));
                    certWriter.writeObject(certParser.readObject());
                    certWriter.close();
                    String certString =
                        new String(Base64.encode(certOS.toString().getBytes()));

                    PEMParser keyParser = new PEMParser(new FileReader(key));
                    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                    PrivateKey pkey =
                        converter.getPrivateKey((PrivateKeyInfo)keyParser.readObject());
                    Signature signature = Signature.getInstance("SHA1WithRSA", "BC");
                    signature.initSign(pkey);
                    signature.update(FedmsgMessage.this.toJson().toString().getBytes());
                    byte[] signed = signature.sign();
                    String signatureString = new String(Base64.encode(signed));

                    return Either.right(
                        new SignedFedmsgMessage(FedmsgMessage.this, signatureString, certString));
                } catch (Exception e) {
                    return Either.left(e);
                }
            }
        };
    }
}
