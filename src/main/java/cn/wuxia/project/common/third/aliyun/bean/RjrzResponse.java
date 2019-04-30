package cn.wuxia.project.common.third.aliyun.bean;


/**
 * //{"Data":{"SecondCheckResult":2},"ErrorMsg":"success","ErrorCode":0}
 * //{"ErrorMsg":"[sessionId] is invalid","ErrorCode":400}
 */
public class RjrzResponse {

    Data Data;

    String ErrorMsg;

    Integer ErrorCode;


    public RjrzResponse.Data getData() {
        return Data;
    }

    public void setData(RjrzResponse.Data data) {
        Data = data;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer errorCode) {
        ErrorCode = errorCode;
    }

    public class Data {
        Integer SecondCheckResult;

        public Integer getSecondCheckResult() {
            return SecondCheckResult;
        }

        public void setSecondCheckResult(Integer secondCheckResult) {
            SecondCheckResult = secondCheckResult;
        }
    }


    public boolean isSuccess() {
        if (ErrorCode != null && ErrorCode == 0) {
            return true;
        }
        return false;
    }
}
