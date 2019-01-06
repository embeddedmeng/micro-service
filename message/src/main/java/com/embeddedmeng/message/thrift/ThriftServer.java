package com.embeddedmeng.message.thrift;

import com.embeddedmeng.messageapi.service.thrift.MessageService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ThriftServer {

    @Value("${server.port}")
    private int servicePort;

    @Autowired
    private MessageService.Iface iface;

    @PostConstruct
    public void startThriftServer() {

        // 方式一 NIO方式
        TProcessor processor = new MessageService.Processor<>(iface);

        TNonblockingServerSocket socket = null;
        try {
            socket = new TNonblockingServerSocket(servicePort);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        // NIO方式
        TNonblockingServer.Args args = new TNonblockingServer.Args(socket);
        args.processor(processor);
        args.transportFactory(new TFramedTransport.Factory());
        args.protocolFactory(new TBinaryProtocol.Factory());
        System.out.println("the serveris started and is listening at 7000...");
        TServer server = new TNonblockingServer(args);
        server.serve();

        // 方式二 线程池方式
//        try {
//            // 设置协议工厂为 TBinaryProtocol.Factory
//            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
//            // 关联处理器与 Hello 服务的实现
//            TMultiplexedProcessor processor = new TMultiplexedProcessor();
//            TServerTransport t = new TServerSocket(7000);
//            // 线程池方式
//            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(t).processor(processor));
//            processor.registerProcessor(MessageService.class.getSimpleName(), new MessageService.Processor<MessageService.Iface>(new MessageServiceImpl()));
////         TSimpleServer server = new TSimpleServer(new Args(t).processor(processor));
//            System.out.println("the serveris started and is listening at 9090...");
//            server.serve();
//        } catch (TTransportException e) {
//            e.printStackTrace();
//        }
    }

}
