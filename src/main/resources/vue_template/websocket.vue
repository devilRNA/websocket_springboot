<!-- vue + websocket连接demo -->
<template>
  <div>
    <h1>vue + websocket连接demo</h1>
    <el-input v-model="text"></el-input>
    <Button @click="test">test_发送字符串</Button>
    <Button @click="test_obj">test_发送对象</Button>
  </div>
</template>

<script>
let socket;
// 给服务器发送一个字符串:
export default {
  data() {
    return {
      // 连接标志位
      lockReconnect: false,
      wsCfg: {
        // websocket地址
        url: "ws://localhost:8080/test/oneToMany"
      },
      text:""
    };
  },
  methods: {
    createWebSocket() {
      try {
        // 创建Web Socket 连接
        socket = new WebSocket(this.wsCfg.url);
        // 初始化事件
        this.initEventHandle(socket);
      } catch (e) {
        // 出错时重新连接
        this.reconnect(this.wsCfg.url);
      }
    },
    initEventHandle(socket) {
      // 连接关闭时触发
      socket.onclose = () => {
        console.log("连接关闭");
      };
      // 通信发生错误时触发
      socket.onerror = () => {
        // 重新创建长连接
        this.reconnect();
      };
      // 连接建立时触发
      socket.onopen = () => {
        console.log("连接成功");
      };
      // 客户端接收服务端数据时触发
      socket.onmessage = msg => {
        // 业务逻辑处理
        console.log(msg.data, "ws:data");
        this.$notify({
          title: '有新消息啦！',
          type: 'success',
          message: msg.data,
          duration: 5000,
        })
      };
    },
    reconnect() {
      if (this.lockReconnect) {
        return;
      }
      this.lockReconnect = true;



      // 没连接上会一直重连，设置延迟避免请求过多
      setTimeout(() => {
        this.lockReconnect = false;
        this.createWebSocket(this.wsCfg.url);
      }, 2000);
    },
    test() {
      // 给服务器发送一个字符串:
      // ws.send("Hello!");
      // socket.send("Hello!");
      socket.send(this.text)
    },
    test_obj(){
      var obj={
        id:1,
        text:this.text
      }
      socket.send(JSON.stringify(obj))
    }
  },
  mounted() {
    this.createWebSocket();
  }
};
</script>
