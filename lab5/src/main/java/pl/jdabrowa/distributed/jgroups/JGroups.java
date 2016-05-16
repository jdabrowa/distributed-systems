package pl.jdabrowa.distributed.jgroups;

import jdk.nashorn.internal.runtime.ListAdapter;
import org.apache.commons.lang3.StringUtils;
import org.jgroups.JChannel;
import org.jgroups.Receiver;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import org.springframework.stereotype.Component;
import pl.jdabrowa.distributed.jgroups.communication.ListenerAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class JGroups {

    public static final String MULTICAST_IP_PREFIX = "230.0.0.";

    public JChannel newJChannel() {
        return new JChannel(false);
    }

    public ProtocolStack createRequiredProtocolStack(String channelName) throws Exception {
        ProtocolStack stack = new ProtocolStack();
        UDP udp = createUDP(channelName);
        stack.addProtocol(udp)
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
        return stack;
    }

    public Receiver newReceiverFor(JChannel channel) {
        return new ListenerAdapter(channel);
    }

    private UDP createUDP(String channelNumber) throws UnknownHostException {
        UDP udp = new UDP();
        if(StringUtils.isNotBlank(channelNumber)) {
            udp.setMulticastAddress(multicastForChannel(channelNumber));
        }
        return udp;
    }

    private InetAddress multicastForChannel(String channelNumber) throws UnknownHostException {
        String addressString = MULTICAST_IP_PREFIX + channelNumber;
        return InetAddress.getByName(addressString);
    }
}
