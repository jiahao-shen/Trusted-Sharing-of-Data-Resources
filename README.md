# 数据资源可信共享

## 注意事项

### Git相关
- 不建议多人同时直接在线上服务器编写, 还是建议先各自在本地编写测试完并push到Github后再进行上线
- 养成好习惯, commit和push前先pull一下, 自己在本地解决好冲突问题

### 项目相关
- fabric-realty的版本为1.14, 服务器上安装的版本为1.18 
- 当使用第三方库或依赖时需要注意
    - 可以通过单元测试, 但进行docker部署时需要进行打包
    - 打包方式1: 先执行`go mod tidy`, 再运行`go mod vendor`, 打包完后需要删除多余的依赖, 仅保留第三方包即可
    - 运行`chaincode_test.go`时需要删除`vendor`目录
- 智能合约执行需要保证结果的一致性, 因此不建议将设计id和time生成的内容放进合约中
- 使用fabric-sdk-go调用合约时, 不用再进行docker部署, 直接在`application/server`目录下, 运行`go run main.go`即可, 相关配置文件为同目录下的`test.yaml`
- 当容器需要访问宿主机时, 可以直接访问`host.docker.internal`或者`172.17.0.1`
- 对于Linux系统, 如果合约需要调用http请求, 会报错`fatal error: unexpected signal during runtime execution`, 并导致容器崩溃, 需要修改如下内容(有时work有时依旧不行, 疯了)
    - 修改`docker-compose.yaml`和`docker-compose-base.yaml`中的`GODEBUG=netdns=1`
    - 在`network/start.sh`最后添加如下内容:
    ```UpdateResolvConf="cp /etc/resolv.conf temp.conf; sed -i '$ d' temp.conf; cp -f temp.conf /etc/resolv.conf; cat /etc/resolv.conf"
        docker exec $(docker ps -aq --filter name=cli) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=orderer.qq.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=dev-peer0.jd.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=^/peer0.jd.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=^/peer1.jd.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=dev-peer0.taobao.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=^/peer0.taobao.com) bash -c "$UpdateResolvConf"
        docker exec $(docker ps -aq --filter name=^/peer1.taobao.com) bash -c "$UpdateResolvConf"
    ```
## TODO
- Nothing

## 问题汇总
- Nothing

## 成员
- 王帅
- 陈荣山
- 崔正龙
- 沈嘉浩
- 李一鸣