package org.fedoraproject.fedmsg;

import org.zeromq.ZMQ;

import java.io.IOException;

import fj.F;
import fj.Unit;
import fj.data.Either;
import fj.data.IO;

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

    /**
     * @return a FedmsgConnection wrapped in an (impure) IO monad.
     */
    public IO<FedmsgConnection> connect() {
        return new IO<FedmsgConnection>() {
            public FedmsgConnection run() {
                FedmsgConnection.this.context = ZMQ.context(1);
                FedmsgConnection.this.sock = context.socket(ZMQ.PUB);
                FedmsgConnection.this.sock.setLinger(FedmsgConnection.this.linger);
                FedmsgConnection.this.sock.connect(FedmsgConnection.this.endpoint);
                return FedmsgConnection.this;
            }
        };
    }

    public IO<Either<Exception, Unit>> send(final FedmsgMessage msg) {
        return new IO<Either<Exception, Unit>>() {
            public Either<Exception, Unit> run() {
                try {
                    FedmsgConnection.this.sock.send(msg.getTopic(), ZMQ.SNDMORE | ZMQ.NOBLOCK);
                    FedmsgConnection.this.sock.send(msg.toJson().toString(), ZMQ.NOBLOCK);
                    return Either.right(Unit.unit());
                } catch (Exception e) {
                    return Either.left(e);
                }
            }
        };
    }
}
