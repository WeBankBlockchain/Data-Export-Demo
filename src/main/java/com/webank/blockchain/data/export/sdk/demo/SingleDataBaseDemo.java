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
public class SingleDataBaseDemo {


    public static void main(String[] args) throws ConfigException, InterruptedException {
        MysqlDataSource mysqlDataSourc = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .pass("password")
                .user("username")
                .build();
        List<MysqlDataSource> mysqlDataSourceList = new ArrayList<>();
        mysqlDataSourceList.add(mysqlDataSourc);
        ExportDataSource dataSource = ExportDataSource.builder()
                .mysqlDataSources(mysqlDataSourceList)
                .autoCreateTable(true)
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
