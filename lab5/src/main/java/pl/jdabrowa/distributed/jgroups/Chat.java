package pl.jdabrowa.distributed.jgroups;

import org.jgroups.JChannel;
import org.jgroups.protocols.MERGE2;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import org.springframework.stereotype.Component;

@Component
public class Chat {

    public void JoinChannel(int number) {
        JChannel channel = new JChannel(false);

    }

    private ProtocolStack createRequiredProtocolStack() throws Exception {
        ProtocolStack stack = new ProtocolStack();
        stack.addProtocol(new UDP())
                .addProtocol(new PING())
                .addProtocol(new MERGE2())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL().setValue("timeout", 12000).setValue("interval", 3000))
                .addProtocol(new VERIFY_SUSPECT())
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK())
                .addProtocol(new UNICAST2())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new FRAG2())
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FLUSH());
        stack.init();
        return stack;
    }
}
