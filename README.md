# 数据资源可信共享

## 注意事项

### Git相关
1. 不建议多人同时直接在线上服务器编写, 还是建议先各自在本地编写测试完并push到Github后再进行上线
2. 养成好习惯, commit和push前先pull一下, 自己在本地解决好冲突问题

### 项目相关
1. fabric-realty的版本为1.14, 服务器上安装的版本为1.18 
2. 当使用第三方库或依赖时需要注意:
    - 可以通过`chaincode_test.go`单元测试, 但进行docker部署时需要进行打包
    - 打包方式: 先执行`go mod tidy`, 再运行`go mod vendor`, 打包完后需要删除多余的依赖, 仅保留第三方包即可
    - 运行`chaincode_test.go`时需要删除`vendor`目录
3. 当使用多个节点进行背书策略时, 需要保证执行结果的一致性, 因此不建议将id或者time的生成放进智能合约中
4. 测试fabric-sdk-go调用合约时, 不必再使用docker进行部署, 可以直接在本地运行, 具体操作如下:
    - 将`application/server/blockchain/sdk.go`文件中的配置文件替换为`test.yaml`
    - 进入`application/server`目录下, 直接运行`go run main.go`
    - 实际部署时记得将配置文件替换为原来的`config.yaml`
5. 当docker容器需要访问外部宿主机时, 可以使用域名`host.docker.internal`或者`172.17.0.1`地址
6. 对于Linux系统, 如果合约调用的http请求中包含域名, 会报错并导致docker容器崩溃, 该问题产生的原因参考[链接](https://developer.aliyun.com/article/238940), 解决方法如下:
    - 修改`network/docker-compose.yaml`和`network/docker-compose-base.yaml`中的`GODEBUG=netdns=1`
    - 在`network/start.sh`文件末尾添加如下内容:
    ```
    echo "修改DNS配置文件"
    UpdateResolvConf="cp /etc/resolv.conf temp.conf; sed -i '$ d' temp.conf; cp -f temp.conf /etc/resolv.conf; cat /etc/resolv.conf"
    docker exec $(docker ps -aq --filter name=cli) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=orderer.qq.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=dev-peer0.jd.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=^/peer0.jd.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=^/peer1.jd.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=dev-peer0.taobao.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=^/peer0.taobao.com) bash -c "$UpdateResolvConf"
    docker exec $(docker ps -aq --filter name=^/peer1.taobao.com) bash -c "$UpdateResolvConf"
    ```
    - 以上操作无法完全解决该问题(仍然会随机产生), 最好的解决办法是往链码容器`dev-peer`中添加环境变量`GODEBUG=netdns=1`, 但当前暂时无法得知该容器的生成方式, 因此无法修改
    - 当前解决方案: 直接使用IP地址进行http请求, 绕过域名解析

## TODO
- Organization添加所属节点
- 调研智能合约能否分配节点
- 调研操作传输是否可行

## 成员
- 王帅
- 陈荣山
- 崔正龙
- 沈嘉浩
- 李一鸣
