图灵北冥:"微信的原始代码也是这几行 !"。

第一步(二三...步以此类推，看序号)  -> 1、TuringIMApplication

2、IMServer.start();

3、IMServer的initChannel()回调

4、自定义一个消息处理WebSocketHandler

5、要保存映射关系的话，在建立链接时，要把状态信息存起来，
    这时我们首先要定义一个模型(或者说指令)，即Command。

6、新建一个指令Command的类型枚举CommandType
(p.s: Command的code有几种类型呢? 我们用CommandType给它枚举出来)

7、再回到WebSocketHandler去解析TextWebSocketFrame

8、根据不同CommandType类型的请求，新建不同类型的Handler，

比如ChatHandler和ConnectionHandler。

9、服务端返给前端消息也需要一个json，这时就写一个返回体Result。

10、在ConnectionHandler里写它应该做的事情，即保存映射关系。

11、要实现消息的发送，目前的Command无法满足需要，还得新增一些指令，创建ChatMessage。

12、再去建一个ChatMessage的type的枚举类MessageType。

