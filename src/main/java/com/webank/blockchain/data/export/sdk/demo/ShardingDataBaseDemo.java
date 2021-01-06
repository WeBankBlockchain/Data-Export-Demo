package com.webank.blockchain.data.export.sdk.demo;

import com.webank.blockchain.data.export.ExportDataSDK;
import com.webank.blockchain.data.export.common.entity.ChainInfo;
import com.webank.blockchain.data.export.common.entity.ExportDataSource;
import com.webank.blockchain.data.export.common.entity.MysqlDataSource;
import com.webank.blockchain.data.export.task.DataExportExecutor;
import org.fisco.bcos.sdk.config.exceptions.ConfigException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/6
 */
public class ShardingDataBaseDemo {

    public static void main(String[] args) throws ConfigException, InterruptedException {
        //配置其中[]内容配置即可测试
        MysqlDataSource mysqlDataSourc = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .pass("password")
                .user("username")
                .build();
        MysqlDataSource mysqlDataSourc1 = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .pass("password")
                .user("username")
                .build();
        List<MysqlDataSource> mysqlDataSourceList = new ArrayList<>();
        mysqlDataSourceList.add(mysqlDataSourc);
        mysqlDataSourceList.add(mysqlDataSourc1);
        ExportDataSource dataSource = ExportDataSource.builder()
                .mysqlDataSources(mysqlDataSourceList)
                .autoCreateTable(true)
                .sharding(true)
                .shardingNumberPerDatasource(2)
                .build();
        DataExportExecutor exportExecutor = ExportDataSDK.create(dataSource, ChainInfo.builder()
                .nodeStr("[ip]:[port]")
                .certPath("config/user_0") // chain certificate path config
                .groupId(1).build());
        ExportDataSDK.start(exportExecutor);
        Thread.sleep(60 *1000L);
//        ExportDataSDK.stop(exportExecutor);
    }
}
