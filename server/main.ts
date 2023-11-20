import { readFileSync } from "node:fs";
import { createServer } from "node:https";
import { ServerResponse, IncomingMessage } from "node:http";

import Chatroom from "./chatroom.ts";

const key = readFileSync("./certificate/key.pem", "utf8");
const cert = readFileSync("./certificate/cert.pem", "utf8");

function handleRequest(_: IncomingMessage, res: ServerResponse) {
    res.writeHead(200);
    res.end("Test message");
}

const server = createServer({ cert, key }, handleRequest);
const chatroom = new Chatroom({
    maxMsgLen: 500,
    password: "test",
    heartbeat: 10 * 1000,
    server,
});
chatroom.init();
server.listen(3000);
