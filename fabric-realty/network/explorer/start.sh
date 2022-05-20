#!/bin/bash

priv_sk_path=$(ls ../crypto-config/peerOrganizations/taobao.com/users/Admin\@taobao.com/msp/keystore/)

cp -rf ./connection-profile/network_temp.json ./connection-profile/network.json

if [[ `uname` == 'Darwin' ]]; then
    echo "Mac OS"
    gsed -i "s/priv_sk/$priv_sk_path/" ./connection-profile/network.json
fi
if [[ `uname` == 'Linux' ]]; then
    echo "Linux"
    sed -i "s/priv_sk/$priv_sk_path/" ./connection-profile/network.json
fi

docker-compose down -v
docker-compose up -d