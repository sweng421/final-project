// @deno-types="ws-types"
import { WebSocket, WebSocketServer } from "ws";
import Chatter from "./chatter.ts";

export interface ChatroomSettings {
    password?: string;
    heartbeat: number;
    maxMsgLen: number;
}

const defaultSettings: ChatroomSettings = {
    maxMsgLen: 500,
    heartbeat: 10 * 1000,
};

export default class Chatroom {
    private server: WebSocketServer;
    private clientData: Map<WebSocket, Chatter>;
    private usernames: Set<string>;
    private settings: ChatroomSettings;
    private intervalId: number | null = null;

    constructor(settings: ChatroomSettings = defaultSettings) {
        this.server = new WebSocketServer({
            path: "/chatroom",
            port: 8080,
        });
        this.clientData = new Map();
        this.usernames = new Set<string>();
        this.settings = settings;
    }

    init() {
        this.server.on("connection", this.onConnect.bind(this));
        this.server.on("error", this.onError.bind(this));
        this.server.on("close", this.onClose.bind(this));
        this.intervalId = setInterval(() => {
            this.broadcastHeartbeat();
        }, this.settings.heartbeat);
    }

    get loginRequired() {
        return !!this.settings.password;
    }

    tryClaimUsername(s: string): boolean {
        if (
            s.length < this.settings.maxMsgLen && /^\w+$/.test(s) &&
            !this.usernames.has(s)
        ) {
            this.usernames.add(s);
            return true;
        }
        return false;
    }

    verifyLogin(password: string): boolean {
        if (!this.settings.password) {
            return false;
        }
        return this.settings.password === password;
    }

    broadcast(messageObj: object) {
        for (const client of this.clientData.values()) {
            client.send(messageObj)
                .catch((_e) => {
                    console.error(
                        "Failed to send message." +
                            "  Trying again",
                    );
                    return client.send(messageObj);
                })
                .catch((_e) => {
                    console.error("Couldn't resend message");
                });
        }
    }

    private onConnect(socket: WebSocket, _req: object) {
        const chatter = new Chatter(this, socket);
        chatter.init();
        this.clientData.set(socket, chatter);
    }

    cleanupChatter(socket: WebSocket) {
        const client = this.clientData.get(socket);
        if (client) {
            const name = client.name;
            this.clientData.delete(socket);
            if (name) {
                this.usernames.delete(name);
            }
        }
    }

    private onError(_socket: WebSocket, err: Error) {
        console.error(err);
    }

    private broadcastHeartbeat() {
        for (const c of this.clientData.values()) {
            if (!c.consumePing()) {
                c.close();
            } else {
                c.ping();
            }
        }
    }

    private onClose() {
        if (this.intervalId !== null) {
            clearInterval(this.intervalId);
        }
    }
}