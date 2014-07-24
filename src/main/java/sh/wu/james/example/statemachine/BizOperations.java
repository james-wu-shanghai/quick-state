package sh.wu.james.example.statemachine;

public interface BizOperations {
    BizOperations create();

    BizOperations update();

    BizOperations submit();

    BizOperations refuse();

    BizOperations reject();

    BizOperations accept();

    BizOperations cancel();

    BizOperations finishInvestment();

    BizOperations cancelInvestment();

    BizOperations acceptCertify();

    BizOperations timeoutCertify();

    BizOperations certify();

    BizOperations rejectCertify();

    BizOperations refuseCertify();

    BizOperations cancelCeritfy();

}
