package com.webank.blockchain.data.export.sdk.demo;

import com.webank.blockchain.data.export.ExportDataSDK;
import com.webank.blockchain.data.export.common.entity.ChainInfo;
import com.webank.blockchain.data.export.common.entity.ContractInfo;
import com.webank.blockchain.data.export.common.entity.ExportConfig;
import com.webank.blockchain.data.export.common.entity.ExportDataSource;
import com.webank.blockchain.data.export.common.entity.MysqlDataSource;
import com.webank.blockchain.data.export.common.entity.StashInfo;
import com.webank.blockchain.data.export.task.DataExportExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/3/8
 */
public class StashClientDemo {

    public static void main(String[] args) throws Exception {
        //数据库配置信息
        MysqlDataSource mysqlDataSourc = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .user("username")
                .pass("password")
                .build();
        //mysql数据库列表
        List<MysqlDataSource> mysqlDataSourceList = new ArrayList<>();
        //mysql数据库添加
        mysqlDataSourceList.add(mysqlDataSourc);
        //导出数据源配置
        ExportDataSource dataSource = ExportDataSource.builder()
                //设置mysql源
                .mysqlDataSources(mysqlDataSourceList)
                //自动建表开启
                .autoCreateTable(true)
                .build();
        //数据导出执行器构建
        DataExportExecutor exportExecutor = ExportDataSDK.create(dataSource, StashInfo.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .user("username")
                .pass("password")
                // chain cryptoType, gm-1
                .cryptoTypeConfig(0)
                .build());
        //数据导出执行启动
        ExportDataSDK.start(exportExecutor);
        //休眠一定时间，因导出执行为线程池执行，测试时主线程需阻塞
        Thread.sleep(60 *1000L);
        //数据导出执行关闭
        //ExportDataSDK.stop(exportExecutor);
    }

}
