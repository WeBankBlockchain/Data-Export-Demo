/**
 * Copyright 2020 Webank.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.blockchain.data.export.utils;

import com.webank.blockchain.data.export.common.entity.ContractInfo;
import com.webank.blockchain.data.export.common.entity.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * PropertiesUtils
 *
 * @author maojiayu
 * @Description: PropertiesUtils
 * @data Dec 28, 2018 4:10:49 PM
 */
@Slf4j
@Component
public class PropertiesUtils {

    private static InputStream stream;
    private static Properties properties = new Properties();

    static {
        stream = PropertiesUtils.class.getClassLoader().getResourceAsStream("classpath:application.properties");
        if (stream == null){
            stream = PropertiesUtils.class.getClassLoader().getResourceAsStream("file:./config/application.properties");
            log.info("find application config in ./config/application.properties");
        }
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return the first mapping result of args.
     *
     * @return property value
     */
    public static String getProperty(String... args) {
        StringBuilder key = new StringBuilder(args[0]);
        for (int i = 1 ; i < args.length; i++) {
            key.append(".").append(args[i]);
        }
        return properties.getProperty(key.toString());
    }


    public static List<MysqlDataSource> getMysqlConfigs() {
        List<MysqlDataSource> dataSources = new ArrayList<>();
        int i = 0;
        while (true) {
            String dbUrl = PropertiesUtils.getProperty("system", "db" + i, "dbUrl");
            if (StringUtils.isBlank(dbUrl)) {
                break;
            }
            String user = PropertiesUtils.getProperty("system", "db" + i, "user");
            String password = PropertiesUtils.getProperty("system", "db" + i, "password");
            MysqlDataSource mysqlDataSource = MysqlDataSource.builder()
                    .jdbcUrl(dbUrl)
                    .user(user)
                    .pass(password)
                    .build();
            dataSources.add(mysqlDataSource);
            i++;
        }
        return dataSources;
    }


    public static List<ContractInfo> getContractInfos() {
        List<ContractInfo> dataSources = new ArrayList<>();
        int i = 0;
        while (true) {
            String contractName = PropertiesUtils.getProperty("system", "contract" + i, "contractName");
            if (StringUtils.isBlank(contractName)) {
                break;
            }
            String abi = PropertiesUtils.getProperty("system", "contract" + i, "abi");
            String binary = PropertiesUtils.getProperty("system", "contract" + i, "binary");
            ContractInfo contractInfo = new ContractInfo()
                    .setBinary(binary)
                    .setAbi(abi)
                    .setContractName(contractName);
            dataSources.add(contractInfo);
            i++;
        }
        return dataSources;
    }

    public static Map<String, List<String>> getGeneratedOff() {
        String generatedOffStr = PropertiesUtils.getProperty("system", "generatedOffStr");
        if (generatedOffStr == null) {
            return null;
        }
        Map<String, List<String>> map = new HashMap<>();
        String[] strings = generatedOffStr.split("\\|");
        for (String str : strings) {
            String[] attributes = str.split("\\.");
            String contractName = attributes[0];
            String[] names = attributes[1].split(",");
            if (!map.containsKey(contractName)) {
                List<String> nameList = Arrays.asList(names);
                map.put(contractName, nameList);
            }
            map.get(contractName).addAll(Arrays.asList(names));
        }
        return map;
    }

    public static Map<String, Map<String, List<String>>> getIgnoreParam() {
        String ignoreParam = PropertiesUtils.getProperty("system", "ignoreParam");
        if (ignoreParam == null) {
            return null;
        }
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        String[] strings = ignoreParam.split("\\|");
        for (String str : strings) {
            String[] attributes = str.split("\\.");
            String contractName = attributes[0];
            String methodName = attributes[1];
            String[] names = attributes[2].split(",");
            Map<String, List<String>> methodMap;
            if (!map.containsKey(contractName)) {
                methodMap = new HashMap<>();
                map.put(contractName, methodMap);
            }
            methodMap = map.get(contractName);
            if (!methodMap.containsKey(methodName)) {
                List<String> nameList = Arrays.asList(names);
                methodMap.put(methodName, nameList);
            }

            map.get(contractName).get(methodName).addAll(Arrays.asList(names));
        }
        return map;
    }

    public static Map<String, Map<String, Map<String, String>>> getparamSQLType() {
        String paramSQLType = PropertiesUtils.getProperty("system", "paramSQLType");
        if (paramSQLType == null) {
            return null;
        }
        Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
        String[] strings = paramSQLType.split("\\|");
        for (String str : strings) {
            String[] attributes = str.split("\\.");
            String contractName = attributes[0];
            String methodName = attributes[1];
            String solidityParamName = attributes[2];
            String paramType = attributes[3];
            Map<String, Map<String, String>> methodMap;
            if (!map.containsKey(contractName)) {
                methodMap = new HashMap<>();
                map.put(contractName, methodMap);
            }
            methodMap = map.get(contractName);
            Map<String, String> paramMap;
            if (!methodMap.containsKey(methodName)) {
                paramMap = new HashMap<>();
                methodMap.put(methodName, paramMap);
                paramMap.put(solidityParamName, paramType);
            }
            map.get(contractName).get(methodName).put(solidityParamName, paramType);
        }
        return map;
    }


}
