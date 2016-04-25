package pl.jdabrowa.agh.distributed.ice.server;

import Ice.Current;
import lombok.Getter;
import pl.jdabrowa.agh.distributed.ice.generated.Ex1._SimpleStringOperationDisp;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleOperationServant extends _SimpleStringOperationDisp {

    private static final AtomicInteger servantId = new AtomicInteger(0);

    @Getter
    private final Date createTime;

    @Getter
    private final int id;

    public SimpleOperationServant() {
        this.createTime = new Date();
        this.id = servantId.updateAndGet(oldId -> ++oldId);
    }

    SimpleOperationServant(Date createTime, int id) {
        this.createTime = createTime;
        this.id = id;
    }

    @Override
    public String invoke(String message, Current __current) {
        StringBuilder responseBuilder = new StringBuilder("Processed messsage: '").append(message).append("'");
        responseBuilder.append("\n");
        responseBuilder.append("Servant id: ").append(servantId);
        responseBuilder.append("\n");
        responseBuilder.append("Servant created at: ").append(createTime.toString());
        return responseBuilder.toString();
    }
}