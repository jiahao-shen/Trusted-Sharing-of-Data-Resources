package api

import (
	"chaincode/model"
	"chaincode/pkg/utils"
	"encoding/json"
	"fmt"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func CreateFileOperation(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 2 {
		return shim.Error("参数个数不满足")
	}
	filePath := args[0]
	operationType := args[1]
	if filePath == "" {
		return shim.Error("参数存在空值")
	}
	createTime, _ := stub.GetTxTimestamp()
	createTimeString := time.Unix(int64(createTime.GetSeconds()), int64(createTime.GetNanos())).Local().Format("2006-01-02 15:04:05")
	fileOperation := &model.FileOperation{
		FilePath:   filePath,
		Type:       operationType,
		CreateTime: createTimeString,
		Hash:       filePath + operationType + createTimeString,
	}
	// 写入账本, 复合主键为文件路径和文件的哈希
	if err := utils.WriteLedger(fileOperation, stub, model.FileOperationKey, []string{fileOperation.FilePath, fileOperation.Hash}); err != nil {
		return shim.Error(fmt.Sprintf("%s", err))
	}
	//将成功创建的信息返回
	fileOperationByte, err := json.Marshal(fileOperation)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化成功创建的信息出错: %s", err))
	}
	// 成功返回
	return shim.Success(fileOperationByte)
}

func QueryFileOperationList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var fileOperationList []model.FileOperation
	results, err := utils.GetStateByPartialCompositeKeys2(stub, model.FileOperationKey, args)
	if err != nil {
		return shim.Error(fmt.Sprintf("%s", err))
	}
	for _, v := range results {
		if v != nil {
			var fileOperation model.FileOperation
			err := json.Unmarshal(v, &fileOperation)
			if err != nil {
				return shim.Error(fmt.Sprintf("QuerySellingList-反序列化出错: %s", err))
			}
			fileOperationList = append(fileOperationList, fileOperation)
		}
	}
	fileOperationListByte, err := json.Marshal(fileOperationList)
	if err != nil {
		return shim.Error(fmt.Sprintf("QuerySellingList-序列化出错: %s", err))
	}
	return shim.Success(fileOperationListByte)
}
