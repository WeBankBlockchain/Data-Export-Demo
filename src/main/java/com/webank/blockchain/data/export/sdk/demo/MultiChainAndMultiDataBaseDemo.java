package com.webank.blockchain.data.export.sdk.demo;

import com.webank.blockchain.data.export.ExportDataSDK;
import com.webank.blockchain.data.export.common.entity.ChainInfo;
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
public class MultiChainAndMultiDataBaseDemo {

    public static void main(String[] args) throws Exception {
        //数据库配置信息
        MysqlDataSource mysqlDataSourc1 = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]?autoReconnect=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8")
                .user("username")
                .pass("password")
                .build();
        //mysql数据库列表
        List<MysqlDataSource> mysqlDataSourceList1 = new ArrayList<>();
        //mysql数据库添加
        mysqlDataSourceList1.add(mysqlDataSourc1);
        //导出数据源配置
        ExportDataSource dataSource1 = ExportDataSource.builder()
                //设置mysql源
                .mysqlDataSources(mysqlDataSourceList1)
                //自动建表开启
                .autoCreateTable(true)
                .build();
        //数据导出执行器构建
        DataExportExecutor exportExecutor1 = ExportDataSDK.create(dataSource1, ChainInfo.builder()
                //链节点连接信息
                .nodeStr("[ip]:[port]")
                //链连接证书位置
                .certPath("config_user_0")
                //群组id
                .groupId(1)
                .build());
        //数据导出执行启动
        ExportDataSDK.start(exportExecutor1);


        //数据库配置信息
        MysqlDataSource mysqlDataSourc2 = MysqlDataSource.builder()
                .jdbcUrl("jdbc:mysql://[ip]:[port]/[database]")
                .user("username")
                .pass("password")
                .build();
        //mysql数据库列表
        List<MysqlDataSource> mysqlDataSourceList2 = new ArrayList<>();
        //mysql数据库添加
        mysqlDataSourceList2.add(mysqlDataSourc2);
        //导出数据源配置
        ExportDataSource dataSource2 = ExportDataSource.builder()
                //设置mysql源
                .mysqlDataSources(mysqlDataSourceList2)
                //自动建表开启
                .autoCreateTable(true)
                .build();
        //数据导出执行器构建
        DataExportExecutor exportExecutor2 = ExportDataSDK.create(dataSource2, ChainInfo.builder()
                //链节点连接信息
                .nodeStr("[ip]:[port]")
                //链连接证书位置
                .certPath("config_user_1")
                //群组id
                .groupId(1)
                .build());
        //数据导出执行启动
        ExportDataSDK.start(exportExecutor2);
        //休眠一定时间，因导出执行为线程池执行，测试时主线程需阻塞
        Thread.sleep(60 *1000L);
    }

}
