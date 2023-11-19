import Chatroom from "./chatroom.ts";

const chatroom = new Chatroom({
    maxMsgLen: 500,
    password: "test",
    heartbeat: 20 * 1000,
});
chatroom.init();
