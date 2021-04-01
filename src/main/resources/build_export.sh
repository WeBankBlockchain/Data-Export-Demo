#!/usr/bin/env bash
LANG=zh_CN.UTF-8


function docker_install()
{
	echo "check Docker......"
	docker -v
    if [ $? -eq  0 ]; then
        echo "Docker already installed!"
    else
    	echo "install docker ..."
        curl -sSL https://get.daocloud.io/docker | sh
        echo "install docker...success!"
    fi
    # create share network==bridge
    #docker network create share_network
}

docker_install


# @function: output information log
# @param: content: information message
function LOG_INFO()
{
    local content=${1}
    echo -e "\033[32m"${content}"\033[0m"
}

BASE_DIR=`pwd`
docker pull wangyue168git/dataexport:1.7.2
docker run -d -p 5200:5200  -v "$BASE_DIR"/config/:/config -v "$BASE_DIR"/log/:/log --name export wangyue168git/dataexport:1.7.2
LOG_INFO "data export run success"

v=`grep system.grafanaEnable ./config/application.properties | cut -d'=' -f2`

if [ ${v} == "true" ]; then
    docker pull grafana/grafana
    docker run   -d   -p 3000:3000   --name=grafana   -e "GF_INSTALL_PLUGINS=grafana-clock-panel,grafana-simple-json-datasource"   grafana/grafana
    LOG_INFO "grafana run success"
fi