package api

import (
	"chaincode/model"
	"chaincode/pkg/utils"
	"encoding/json"
	"fmt"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func CreateData(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 11 {
		return shim.Error("参数个数不满足")
	}

	dataID := args[0]
	dataName := args[1]
	dataIntroduction := args[2]
	dataAuthor := args[3]
	dataField := args[4]
	dataType := args[5]
	dataShared := args[6]
	dataClassified := args[7]
	dataVersion := args[8]
	dataLocation := args[9]
	dataCreated := args[10]

	if dataID == "" || dataName == "" || dataAuthor == "" || dataVersion == "" || dataCreated == "" {
		return shim.Error("参数存在空值")
	}

	dataItem := &model.Data{
		ID:           dataID,
		Name:         dataName,
		Introduction: dataIntroduction,
		Author:       dataAuthor,
		Field:        dataField,
		Type:         dataType,
		Shared:       dataShared,
		Classified:   dataClassified,
		Version:      dataVersion,
		Location:     dataLocation,
		Created:      dataCreated,
		Hash:         utils.GetSHA256String(args),
	}

	if err := utils.WriteLedger(dataItem, stub, model.DataKey, []string{dataID}); err != nil {
		return shim.Error(fmt.Sprintf("保存数据项失败:%s", err))
	}

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, []string{dataAuthor})
	if err != nil {
		return shim.Error(fmt.Sprintf("不存在当前机构:%s", err))
	}

	var org model.Organization
	err = json.Unmarshal(results[0], &org)
	if err != nil {
		return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
	}
	org.DataList = append(org.DataList, dataID)

	if err := utils.WriteLedger(org, stub, model.OrganizationKey, []string{org.ID}); err != nil {
		return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
	}

	dataItemByte, err := json.Marshal(dataItem)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(dataItemByte)
}

// 查询数据项目录
func QueryDataList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var dataList []model.Data

	// TODO(获取args)

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, args)
	if err != nil {
		return shim.Error(fmt.Sprintf("%s", err))
	}

	for _, val := range results {
		if val != nil {
			var org model.Organization
			err := json.Unmarshal(val, &org)
			if err != nil {
				return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
			}
			for _, item := range org.DataList {
				results, err := utils.GetStateByPartialCompositeKeys(stub, model.DataKey, []string{item})
				if err != nil {
					return shim.Error(fmt.Sprintf("%s", err))
				}
				var data model.Data
				err = json.Unmarshal(results[0], &data)
				if err != nil {
					return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
				}
				dataList = append(dataList, data)
			}
		}
	}

	dataListByte, err := json.Marshal(dataList)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(dataListByte)
}
