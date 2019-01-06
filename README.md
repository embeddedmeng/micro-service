### 注意
#####common,message-api,user-api需要build后 Lifecycle->install,否则打包会找不到依赖包

#####message服务application.yml文件需要修改

    spring:
        mail:
            host: smtp.163.com  # 163邮件host,自定义配置
            username: xxx@163.com # 163邮箱账号,自定义配置
            password: xxxxxx # 邮箱密码,自定义配置

### 文件结构
#### 服务模块
#####src.main.java.com.embeddedmeng.xxx :
    controller  # controller层：暴露调用接口
        xxxController
    serviceimpl  # serviceimpl层：实现服务方法接口
        xxxServiceImpl 
    client  # client层：feign-http服务间调用，thrift-rpc服务间调用
        feign
        thrift
#####src.main.java.com.embeddedmeng.xxx-api :
    dao  # dao层：定义数据库交互接口
        xxxDao 
    entity  # entity层：定义与数据库表对应的实体类和接收及返回的实体类
        xxx
    service  # service层：定义服务方法接口
        xxxService
        
#####基础通用包common
    base # 自定义基础类
    enums # 常用枚举
    util # 通用工具类


