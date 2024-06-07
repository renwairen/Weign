package com.github.renwairen.weign.contract.executor;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.renwairen.weign.domain.Function;
import com.github.renwairen.weign.util.JsonUtil;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;

import java.io.IOException;

/**
 * 只读，不发送交易，同步等待线上返回值 @Author Zhang La @Created at 2022/7/13 14:16
 */
public class FunctionExecutor {
    private final Function function;

    public FunctionExecutor(Function function) {
        this.function = function;
    }

    public JsonNode execute(ExecuteOption option, Object... args) throws IOException {
        String data = function.encode(args);
        String resp = remoteCall(option, data);
        if (resp == null) {
            return JsonUtil.nullNode();
        }
        return function.decode(resp);
    }

    public String remoteCall(ExecuteOption option, String data)
            throws IOException, ContractCallException {
        Transaction transaction =
                Transaction.createEthCallTransaction(option.getWallet(), option.getContract(), data);
        Request<?, EthCall> request =
                option.genWeb3j().ethCall(transaction, option.getBlockParameter());
        EthCall ethCall = request.send();

        if (ethCall.isReverted()) {
            String message = String.format(TransactionManager.REVERT_ERR_STR, ethCall.getRevertReason());
            throw new ContractCallException(message);
        }
        return ethCall.getValue();
    }
}
