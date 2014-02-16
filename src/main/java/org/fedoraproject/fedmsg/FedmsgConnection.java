package org.fedoraproject.fedmsg;

import org.zeromq.ZMQ;

import java.io.IOException;

/**
 * Connect to a fedmsg bus.
 *
 * @author Ricky Elrod
 * @version 1.0.0
 */
public final class FedmsgConnection {
    private String endpoint;
    private int linger;
    private ZMQ.Socket sock;
    private ZMQ.Context context;

    public FedmsgConnection setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public FedmsgConnection setLinger(int linger) {
        this.linger = linger;
        return this;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public int getLinger() {
        return this.linger;
    }

    public FedmsgConnection connect() {
        this.context = ZMQ.context(1);
        this.sock = context.socket(ZMQ.PUB);
        this.sock.setLinger(this.linger);
        this.sock.connect(this.endpoint);
        return this;
    }

    public void send(SignedFedmsgMessage msg) throws IOException {
        this.sock.send(msg.getMessage().getTopic(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
        this.sock.send(msg.toJson().toString(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
    }

    public void send(FedmsgMessage msg) throws IOException {
        this.sock.send(msg.getTopic(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
        this.sock.send(msg.toJson().toString(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
    }
}
