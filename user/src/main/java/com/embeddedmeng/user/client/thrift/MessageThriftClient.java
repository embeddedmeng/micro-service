package com.embeddedmeng.user.client.thrift;

import com.embeddedmeng.messageapi.service.thrift.MessageService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class MessageThriftClient {

    @Value("${thrift.message.ip}")
    private String ip;

    @Value("${thrift.message.port}")
    private int port;

    private MessageService.Client messageService;
    private TBinaryProtocol protocol;
    private TSocket socket;
    private TTransport tTransport;

    public MessageService.Client getMessageService() throws TTransportException {
        System.out.println("ip = " + ip);
        // 如果添加 try-catch 则异常在方法内处理，不添加try-catch而是添加throws TTransportException 则把异常抛到方法调用层
        socket = new TSocket(ip, port);
        tTransport = new TFramedTransport(socket);
        protocol = new TBinaryProtocol(tTransport);

        messageService = new MessageService.Client(protocol);
        socket.open();
        return messageService;
    }

    public void close() {
        socket.close();
    }

}
