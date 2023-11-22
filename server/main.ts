// @deno-types="express-types"
import express from "express";
import { readFileSync } from "node:fs";
import { createServer } from "node:https";
import { config } from "dotenv";

import Chatroom from "./chatroom.ts";

config();

const keyPath = Deno.env.get("KEY_PATH");
const certPath = Deno.env.get("CERT_PATH");
const chatPath = Deno.env.get("CHAT_PATH") ?? "/chatroom";
const maxMsgLen = parseInt(Deno.env.get("MAX_MSG_LEN") ?? "500");
const maxUsrLen = parseInt(Deno.env.get("MAX_USR_LEN") ?? "10");
const heartbeat = parseInt(Deno.env.get("HEARTBEAT_DURATION") ?? "10000");
const password = Deno.env.get("PWD_HASH");

if (!keyPath || !certPath) {
    console.error("No SSL certificate specified");
    Deno.exit(1);
}

const key = readFileSync(keyPath, "utf8");
const cert = readFileSync(certPath, "utf8");

const router = express()
    .get("/", function(_req, res) {
        res.status(200).send("Chatroom server");
    })
    .get("/settings", function(_req, res) {
        res.status(200).json({
            chatPath,
            maxMsgLen,
            maxUsrLen,
            requiresPassword: !!password,
        });
    });

const server = createServer({ cert, key }, router);
const chatroom = new Chatroom({
    maxMsgLen,
    maxUsrLen,
    heartbeat,
    chatPath,
    server,
    password,
});
chatroom.init();
server.listen(443);
