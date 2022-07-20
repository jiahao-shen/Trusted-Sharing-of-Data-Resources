package main

import (
	"fmt"

	"github.com/hyperledger/fabric-sdk-go/pkg/client/channel"
	"github.com/hyperledger/fabric-sdk-go/pkg/core/config"
	"github.com/hyperledger/fabric-sdk-go/pkg/fabsdk"
)

func main() {
	sdk, err := fabsdk.New(config.FromFile("config.yaml"))
	if err != nil {
		fmt.Println("加载配置文件失败")
		fmt.Println(err)
	}

	ctx := sdk.ChannelContext("medicinechannel", fabsdk.WithUser("Admin"))
	cli, err := channel.New(ctx)
	if err != nil {
		fmt.Println("通道环境失败")
		fmt.Println(err)
	}

	var args [][]byte
	args = append(args, []byte("a"))
	resp, err := cli.Query(channel.Request{
		ChaincodeID: "mycc",
		Fcn:         "query",
		Args:        args,
	}, channel.WithTargetEndpoints("peer0.BHospital.trustchain.com"))
	if err != nil {
		fmt.Println("查询链码失败")
	}

	fmt.Println(string(resp.Payload))

}
