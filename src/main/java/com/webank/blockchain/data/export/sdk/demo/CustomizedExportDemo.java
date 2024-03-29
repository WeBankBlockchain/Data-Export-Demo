package com.webank.blockchain.data.export.sdk.demo;

import com.google.common.collect.Lists;
import com.webank.blockchain.data.export.ExportDataSDK;
import com.webank.blockchain.data.export.common.bo.data.BlockInfoBO;
import com.webank.blockchain.data.export.common.bo.data.TxReceiptRawDataBO;
import com.webank.blockchain.data.export.common.entity.*;
import com.webank.blockchain.data.export.common.subscribe.face.SubscriberInterface;
import com.webank.blockchain.data.export.task.DataExportExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @date 2021/09/30
 */
public class CustomizedExportDemo {
    public static void main(String[] args) throws Exception, InterruptedException {
        MysqlDataSource mysqlDataSourc = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]?autoReconnect=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8")
                .pass("password")
                .user("username")
                .build();
        List<MysqlDataSource> mysqlDataSourceList = new ArrayList<>();
        mysqlDataSourceList.add(mysqlDataSourc);
        ExportDataSource dataSource = ExportDataSource.builder()
                .mysqlDataSources(mysqlDataSourceList)
                .autoCreateTable(true)
                .build();

        ExportConfig exportConfig = new ExportConfig();
        //配置合约信息，可配置多合约
        List<ContractInfo> contractInfoList = Lists.newArrayList();
        exportConfig.setContractInfoList(contractInfoList);
        ContractInfo contractInfo = new ContractInfo()
                .setBinary("608060405234801561001057600080fd5b506040805190810160405280600d81526020017f48656c6c6f2c20576f726c6421000000000000000000000000000000000000008152506000908051906020019061005c929190610062565b50610107565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106100a357805160ff19168380011785556100d1565b828001600101855582156100d1579182015b828111156100d05782518255916020019190600101906100b5565b5b5090506100de91906100e2565b5090565b61010491905b808211156101005760008160009055506001016100e8565b5090565b90565b6102d7806101166000396000f30060806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634ed3885e146100515780636d4ce63c146100ba575b600080fd5b34801561005d57600080fd5b506100b8600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061014a565b005b3480156100c657600080fd5b506100cf610164565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561010f5780820151818401526020810190506100f4565b50505050905090810190601f16801561013c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b8060009080519060200190610160929190610206565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156101fc5780601f106101d1576101008083540402835291602001916101fc565b820191906000526020600020905b8154815290600101906020018083116101df57829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024757805160ff1916838001178555610275565b82800160010185558215610275579182015b82811115610274578251825591602001919060010190610259565b5b5090506102829190610286565b5090565b6102a891905b808211156102a457600081600090555060010161028c565b5090565b905600a165627a7a72305820aa8d37bec7b8a85e32740629893aa0bd0894e6eadefe527bc854f28f9493d1fd0029")
                .setAbi("[{\"constant\":false,\"inputs\":[{\"name\":\"n\",\"type\":\"string\"}],\"name\":\"set\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]")
                .setContractName("HelloWorld");
        contractInfoList.add(contractInfo);
        registerCustomizedEvents(exportConfig);
        DataExportExecutor exportExecutor = ExportDataSDK.create(dataSource, ChainInfo.builder()
                .nodeStr("[ip]:[port]")
                .certPath("config/user_0") // chain certificate path config
                .groupId(1).build(), exportConfig);
        ExportDataSDK.start(exportExecutor);
        Thread.sleep(60 *1000L);
//        ExportDataSDK.stop(exportExecutor);
    }

    private static void registerCustomizedEvents( ExportConfig exportConfig){
        exportConfig.getTopicRegistry().getBlockTopic().addSubscriber(new SubscriberInterface<BlockInfoBO>() {
            @Override
            public boolean shouldProcess(BlockInfoBO blockInfoBO, Object context) {
                return blockInfoBO.getBlockDetailInfo().getBlockHeight() >= 10;
            }

            @Override
            public void process(BlockInfoBO blockInfoBO) {
                System.out.println(blockInfoBO.getBlockDetailInfo().getBlockHash());
            }
        });
        exportConfig.getTopicRegistry().getTxReceiptTopic().addSubscriber(new SubscriberInterface<TxReceiptRawDataBO>() {
            @Override
            public boolean shouldProcess(TxReceiptRawDataBO txReceiptRawDataBO, Object context) {
                return txReceiptRawDataBO.getTo().equals("xxxx");
            }

            @Override
            public void process(TxReceiptRawDataBO txReceiptRawDataBO) {

            }
        });
        //...
    }
}
