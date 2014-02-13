package org.fedoraproject.fedmsg;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;

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

/**
 * @author Ricky Elrod
 */
@JsonPropertyOrder(alphabetic=true)
public class FedmsgMessage {
    private HashMap<String, String> message;
    private String topic;
    private long timestamp;
    private long i; // What is this?

    public FedmsgMessage() { }

    protected FedmsgMessage(FedmsgMessage orig) {
        this.message = orig.getMessage();
        this.topic = orig.getTopic();
        this.timestamp = orig.getTimestamp().getTime();
        this.i = orig.getI();
    }

    public FedmsgMessage setMessage(HashMap<String, String> m) {
        this.message = m;
        return this;
    }

    public FedmsgMessage setTopic(String t) {
        this.topic = t;
        return this;
    }

    public FedmsgMessage setTimestamp(Date t) {
        this.timestamp = t.getTime();
        return this;
    }

    public FedmsgMessage setI(long i) {
        this.i = i;
        return this;
    }

    public HashMap<String, String> getMessage() {
        return this.message;
    }

    public String getTopic() {
        return this.topic;
    }

    public Date getTimestamp() {
        return new Date(this.timestamp);
    }

    public long getI() {
        return this.i;
    }

    public ByteArrayOutputStream toJson() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter w = mapper.writer();
        w.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValue(os, this);
        os.close();
        return os;
    }

    public String sign(File cert, File key)
        throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            SignatureException {
        Security.addProvider(new BouncyCastleProvider());

        PEMParser certParser = new PEMParser(new FileReader(cert));
        ByteArrayOutputStream certOS = new ByteArrayOutputStream();
        PEMWriter certWriter = new PEMWriter(new OutputStreamWriter(certOS));
        certWriter.writeObject(certParser.readObject());
        certWriter.close();

        PEMParser keyParser = new PEMParser(new FileReader(key));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKey pkey =
            converter.getPrivateKey((PrivateKeyInfo)keyParser.readObject());
        Signature signature = Signature.getInstance("SHA1WithRSA", "BC");
        signature.initSign(pkey);
        signature.update(this.toJson().toString().getBytes());
        byte[] signed = signature.sign();
        return (new String(Base64.encode(signed)));
    }
}
