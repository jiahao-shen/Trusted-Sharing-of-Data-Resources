package blockchain

import (
	"github.com/hyperledger/fabric-sdk-go/pkg/client/channel"
	"github.com/hyperledger/fabric-sdk-go/pkg/core/config"
	"github.com/hyperledger/fabric-sdk-go/pkg/fabsdk"
)

// 配置信息
var (
	configPath    = "config.yaml"   // 部署配置文件路径
	sdk           *fabsdk.FabricSDK // Fabric SDK
	channelName   = "appchannel"    // 通道名称
	user          = "Admin"         // 用户
	chainCodeName = "fabric-realty" // 链码名称
)

// Init 初始化
func Init() {
	var err error
	// 通过配置文件初始化SDK
	sdk, err = fabsdk.New(config.FromFile(configPath))
	if err != nil {
		panic(err)
	}
}

// ChannelExecute 区块链交互
func ChannelExecute(fcn string, args [][]byte, endpoints []string) (channel.Response, error) {
	// 默认节点
	if endpoints == nil {
		endpoints = append(endpoints, "peer0.AHospital.trustchain.com")
		endpoints = append(endpoints, "peer0.BHospital.trustchain.com")
	}

	// 创建客户端，表明在通道的身份
	ctx := sdk.ChannelContext(channelName, fabsdk.WithUser(user))
	cli, err := channel.New(ctx)
	if err != nil {
		return channel.Response{}, err
	}
	// 对区块链账本的写操作（调用了链码的invoke）
	resp, err := cli.Execute(channel.Request{
		ChaincodeID: chainCodeName,
		Fcn:         fcn,
		Args:        args,
	}, channel.WithTargetEndpoints(endpoints...))
	if err != nil {
		return channel.Response{}, err
	}
	//返回链码执行后的结果
	return resp, nil
}

// ChannelQuery 区块链查询
func ChannelQuery(fcn string, args [][]byte, endpoints []string) (channel.Response, error) {
	// 默认节点
	if endpoints == nil {
		endpoints = append(endpoints, "peer0.AHospital.trustchain.com")
		endpoints = append(endpoints, "peer0.BHospital.trustchain.com")
	}

	// 创建客户端，表明在通道的身份
	ctx := sdk.ChannelContext(channelName, fabsdk.WithUser(user))
	cli, err := channel.New(ctx)
	if err != nil {
		return channel.Response{}, err
	}
	// 对区块链账本查询的操作（调用了链码的invoke），只返回结果
	resp, err := cli.Query(channel.Request{
		ChaincodeID: chainCodeName,
		Fcn:         fcn,
		Args:        args,
	}, channel.WithTargetEndpoints(endpoints...))
	if err != nil {
		return channel.Response{}, err
	}
	//返回链码执行后的结果
	return resp, nil
}
