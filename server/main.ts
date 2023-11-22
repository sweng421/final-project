import { readFileSync } from "node:fs";
import { createServer } from "node:https";
import { IncomingMessage, ServerResponse } from "node:http";
import { config } from "dotenv";

import Chatroom from "./chatroom.ts";

config();

const keyPath = Deno.env.get("KEY_PATH");
const certPath = Deno.env.get("CERT_PATH");
const chatPath = Deno.env.get("CHAT_PATH") ?? "/chatroom";
const maxMsgLen = parseInt(Deno.env.get("MAX_MSG_LEN") ?? "500");
const maxUsrLen = parseInt(Deno.env.get("MAX_USR_LEN") ?? "10");
const heartbeat = parseInt(Deno.env.get("HEARTBEAT_DURATION") ?? "10000");

if (!keyPath || !certPath) {
    console.error("No SSL certificate specified");
    Deno.exit(1);
}

const key = readFileSync(keyPath, "utf8");
const cert = readFileSync(certPath, "utf8");

function handleRequest(_: IncomingMessage, res: ServerResponse) {
    res.writeHead(200);
    res.end("Test message");
}

const server = createServer({ cert, key }, handleRequest);
const chatroom = new Chatroom({
    maxMsgLen,
    maxUsrLen,
    heartbeat,
    chatPath,
    server,
    password: Deno.env.get("PWD_HASH"),
});
chatroom.init();
server.listen(3000);
