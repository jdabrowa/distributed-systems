package pl.jdabrowa.agh.distributed.ice.server;

import Ice.Current;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1._SimpleStringOperationDisp;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleOperationServant extends _SimpleStringOperationDisp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOperationServant.class);

    private static final AtomicInteger servantId = new AtomicInteger(0);

    @Getter
    private final Date createTime;

    @Getter
    private final int id;

    public SimpleOperationServant() {
        this(new Date(), servantId.updateAndGet(oldId -> ++oldId));
    }

    SimpleOperationServant(Date createTime, int id) {
        LOGGER.debug("Creating servant with id {} and creation date {}", id, createTime.toString());
        this.createTime = createTime;
        this.id = id;
    }

    @Override
    public String invoke(String message, Current __current) {

        LOGGER.trace("Servant Id: {}, Create date: {}", id, createTime.toString());
        LOGGER.trace("Message received: {}", message);

        StringBuilder responseBuilder = new StringBuilder("Processed messsage: '").append(message).append("'");
        responseBuilder.append("\n");
        responseBuilder.append("Servant id: ").append(servantId);
        responseBuilder.append("\n");
        responseBuilder.append("Servant created at: ").append(createTime.toString());
        return responseBuilder.toString();
    }
}
