package cn.wuxia.project.common.api;

public enum MsgStatusEnum {
    INFORMATIONAL(1), SUCCESSFUL(2), REDIRECTION(3), CLIENT_ERROR(4), SERVER_ERROR(5);
    private int status;

    MsgStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "" + status;
    }
}
