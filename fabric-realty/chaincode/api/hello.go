package api

import (
	"chaincode/pkg/utils"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

// Hello 测试
func Hello(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	err := utils.WriteLedger(map[string]interface{}{"msg": "hello"}, stub, "hello", []string{})
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success([]byte("hello world"))
}

func Net(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	resp, err := http.Get("http://www.baidu.com")

	if err != nil {
		return shim.Error(fmt.Sprintf("HTTP请求失败:%s", err))
	}

	defer resp.Body.Close()
	body, _ := ioutil.ReadAll(resp.Body)

	return shim.Success(body)
}
