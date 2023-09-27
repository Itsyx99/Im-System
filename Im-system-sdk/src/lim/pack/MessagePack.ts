import Beans from '../common/utils';

export class MessagePack {
    appId: number;
    messageId: string;
    fromId?: string;
    toId?: string;
    messageRandom?: number;
    messageTime?: number;
    messageBody?: string;

    constructor(appId: number) {
        this.messageId = Beans.uuid();
        this.appId = appId;
        this.messageRandom = this.RangeInteger(0, 10000);
        this.messageTime = Date.parse(new Date().toString());
    }

    RangeInteger(min: number, max: number) {
        const range = max - min
        const value = Math.floor(Math.random() * range) + min
        return value
    }


    buildTextMessagePack(fromId: string, toId: string, text: string) {
        this.fromId = fromId;
        this.toId = toId;
        let body = { type: 1, content: text }
        this.messageBody = Beans.json(body);
    }

    buildCustomerMessagePack(fromId: string, toId: string, type: number, obj: any) {
        this.fromId = fromId;
        this.toId = toId;
        let body = { type: type, content: obj }
        this.messageBody = Beans.json(body);
    }
}