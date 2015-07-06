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
    private final String endpoint;
    private final int linger;
    private ZMQ.Socket sock;
    private ZMQ.Context context;

    public FedmsgConnection(
        String endpoint,
        int linger
    ) {
        this.endpoint = endpoint;
        this.linger = linger;
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

    public boolean disconnect(ZMQ.Socket sock) {
	boolean result = sock.disconnect();
	return result;
    }

    public void send(FedmsgMessage msg) throws IOException {
        this.sock.send(msg.getTopic(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
        this.sock.send(msg.toJson().toString(), ZMQ.NOBLOCK);
    }
}
