// @deno-types="ws-types"
import { WebSocket } from "ws";
import Chatroom from "./chatroom.ts";
import {
    IncomingMessage,
    LoginMessage,
    MessageType,
    PostMessage,
    UsernameMessage,
} from "./messages.ts";

export default class Chatter {
    private readonly socket: WebSocket;
    private nickname: string | null;
    private isAlive: boolean;
    private chatroom: Chatroom;
    private signedIn: boolean;

    constructor(chatroom: Chatroom, socket: WebSocket) {
        this.isAlive = true;
        this.socket = socket;
        this.nickname = null;
        this.chatroom = chatroom;
        this.signedIn = !chatroom.loginRequired;
    }

    init() {
        this.socket.on("message", (msg) => {
            try {
                const msgObj = JSON.parse(msg.toString());
                this.onMessage(msgObj);
            } catch (e) {
                if (e instanceof Error) {
                    this.onError(e);
                }
            }
        });
        this.socket.on("error", this.onError.bind(this));
        this.socket.on("pong", this.onPong.bind(this));
        this.socket.on("close", this.onClose.bind(this));
    }

    send(msg: object): Promise<void> {
        return new Promise((accept, reject) => {
            const body = JSON.stringify(msg);
            this.socket.send(body, (err) => {
                if (err) {
                    reject(err);
                } else {
                    accept();
                }
            });
        });
    }

    private sendError(msg: string): Promise<void> {
        return this.send({
            kind: "error",
            error: msg,
        });
    }

    private confirm(msg: string): Promise<void> {
        return this.send({
            kind: "confirmation",
            message: msg,
        });
    }

    ping() {
        this.socket.ping();
    }

    private onMessage(msg: IncomingMessage) {
        switch (msg.kind) {
            case MessageType.Login:
                this.onLogin(<LoginMessage> msg);
                break;
            case MessageType.Username:
                this.onUsername(<UsernameMessage> msg);
                break;
            case MessageType.Post:
                this.onPost(<PostMessage> msg);
                break;
        }
    }

    private onPost(msg: PostMessage) {
        if (!msg.message) {
            return;
        }
        const message = (msg.message + "").trim();
        if (
            this.nickname && this.signedIn &&
            message.length > 0 &&
            message.length <= this.chatroom.settings.maxMsgLen
        ) {
            this.chatroom.broadcast({
                kind: "message",
                author: this.nickname,
                message, timestamp: Date.now(),
            });
        } else {
            this.sendError("Couldn't send message");
        }
    }

    private async onLogin(msg: LoginMessage) {
        if (this.signedIn) {
            this.sendError("Already signed in");
            return;
        }
        if (!msg.password) {
            this.sendError("No password provided");
            return;
        }
        if (await this.chatroom.verifyLogin(msg.password)) {
            await this.confirm("LOGIN");
            this.signedIn = true;
        } else {
            this.sendError("Invalid login");
        }
    }

    private async onUsername(msg: UsernameMessage) {
        if (!this.signedIn) {
            this.sendError("Not signed in");
            return;
        }
        if (!msg.username) {
            this.sendError("No username provided");
            return;
        }
        if (!this.chatroom.tryClaimUsername(msg.username)) {
            this.sendError("Invalid or taken username");
            return;
        }
        await this.confirm("USERNAME");
        this.nickname = msg.username;
    }

    private onError(err: Error) {
        console.error(err);
    }

    private onPong() {
        this.isAlive = true;
    }

    private onClose() {
        this.chatroom.cleanupChatter(this.socket);
    }

    close() {
        this.socket.terminate();
    }

    consumePing(): boolean {
        const alive = this.isAlive;
        this.isAlive = false;
        return alive;
    }

    get name() {
        return this.nickname;
    }
}
