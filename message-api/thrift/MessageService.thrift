namespace java com.embeddedmeng.messageapi.service.thrift
service MessageService {
    bool sendEmail(1:list<string> toArray, 2:string subject, 3:string text, 4:string filename, 5:string filepath);
}