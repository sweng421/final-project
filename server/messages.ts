export enum MessageType {
    Login = "login",
    Username = "username",
    Post = "post",
}

export interface IncomingMessage {
    kind: MessageType;
}

export interface LoginMessage extends IncomingMessage {
    kind: MessageType.Login;
    password: string;
}

export interface UsernameMessage extends IncomingMessage {
    kind: MessageType.Username;
    username: string;
}

export interface PostMessage extends IncomingMessage {
    kind: MessageType.Post;
    message: string;
}
