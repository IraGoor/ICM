package common.controllers;

import java.io.Serializable;

public class Message implements Serializable {


    private static final long serialVersionUID = -5764940580602574742L;
    private OperationType operationtype;
    private Object object;
    private ReturnMsgType returnmsg;


    public Message(OperationType operationtype, Object object) {
        this.operationtype = operationtype;
        this.object = object;
    }

    public Message(OperationType operationtype, ReturnMsgType returnmsg, Object object) {
        this.operationtype = operationtype;
        this.object = object;
        this.returnmsg = returnmsg;
    }

    public ReturnMsgType getReturnmsg() {
        return returnmsg;
    }

    public void setReturnmsg(ReturnMsgType returnmsg) {
        this.returnmsg = returnmsg;
    }

    public OperationType getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(OperationType operationtype) {
        this.operationtype = operationtype;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "Message [operationtype=" + operationtype + ", object=" + object + "]";
    }
}
