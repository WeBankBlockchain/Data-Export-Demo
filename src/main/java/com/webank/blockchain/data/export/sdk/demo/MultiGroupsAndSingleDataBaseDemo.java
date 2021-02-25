package com.webank.blockchain.data.export.sdk.demo;

import com.webank.blockchain.data.export.ExportDataSDK;
import com.webank.blockchain.data.export.common.entity.ChainInfo;
import com.webank.blockchain.data.export.common.entity.ExportConfig;
import com.webank.blockchain.data.export.common.entity.ExportDataSource;
import com.webank.blockchain.data.export.common.entity.MysqlDataSource;
import com.webank.blockchain.data.export.task.DataExportExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/2/25
 */
public class MultiGroupsAndSingleDataBaseDemo {

    public static void main(String[] args) throws Exception {
        MysqlDataSource mysqlDataSourc = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .user("username")
                .pass("password")
                .build();
        List<MysqlDataSource> mysqlDataSourceList = new ArrayList<>();
        mysqlDataSourceList.add(mysqlDataSourc);
        ExportDataSource dataSource = ExportDataSource.builder()
                .mysqlDataSources(mysqlDataSourceList)
                .autoCreateTable(true)
                .build();
        ExportConfig config1 = new ExportConfig();
        config1.setTablePostfix("_g1_");
        ExportConfig config2 = new ExportConfig();
        config2.setTablePostfix("_g2_");
        //通过rpc通道导出数据，需要打开链上对应rpc端口的监听
        //与channel通道同时设置后，优先RPC通道
        DataExportExecutor exportExecutor1 = ExportDataSDK.create(dataSource, ChainInfo.builder()
                .rpcUrl("http://127.0.0.1:8546")
                // chain cryptoType, gm-1
                .cryptoTypeConfig(0)
                //群组1
                .groupId(1)
                .build(), config1);
        DataExportExecutor exportExecutor2 = ExportDataSDK.create(dataSource, ChainInfo.builder()
                .rpcUrl("http://127.0.0.1:8546")
                // chain cryptoType, gm-1
                .cryptoTypeConfig(0)
                //群组2
                .groupId(2)
                .build(), config2);
        ExportDataSDK.start(exportExecutor1);
        ExportDataSDK.start(exportExecutor2);
        Thread.sleep(60 *1000L);
    }

}
