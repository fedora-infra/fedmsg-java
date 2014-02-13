package org.fedoraproject.fedmsg;

import org.zeromq.ZMQ;

/**
 * @author Ricky Elrod
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
}
