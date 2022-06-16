package api

import (
	"bytes"
	"chaincode/model"
	"chaincode/pkg/utils"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

func CreateAPI(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 10 {
		return shim.Error("参数个数不满足")
	}

	apiName := args[0]
	apiID := args[1]
	apiIntroduction := args[2]
	apiAuthor := args[3]
	apiURL := args[4]
	apiType := args[5]
	apiRequest := args[6]
	apiResponse := args[7]
	apiVersion := args[8]
	apiCreated := args[9]

	if apiName == "" || apiID == "" || apiAuthor == "" || apiURL == "" || apiType == "" || apiVersion == "" || apiCreated == "" {
		return shim.Error("参数存在空值")
	}

	api := &model.API{
		Name:         apiName,
		ID:           apiID,
		Introduction: apiIntroduction,
		Author:       apiAuthor,
		URL:          apiURL,
		Type:         apiType,
		Request:      apiRequest,
		Response:     apiResponse,
		Version:      apiVersion,
		Created:      apiCreated,
		Hash:         utils.GetSHA256String(args),
	}

	if err := utils.WriteLedger(api, stub, model.APIKey, []string{apiID}); err != nil {
		return shim.Error(fmt.Sprintf("保存数据项失败:%s", err))
	}

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.OrganizationKey, []string{apiAuthor})
	if err != nil {
		return shim.Error(fmt.Sprintf("不存在当前机构:%s", err))
	}

	var org model.Organization
	err = json.Unmarshal(results[0], &org)
	if err != nil {
		return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
	}

	org.APIList = append(org.APIList, apiID)

	if err := utils.WriteLedger(org, stub, model.OrganizationKey, []string{org.ID}); err != nil {
		return shim.Error(fmt.Sprintf("更新索引失败:%s", err))
	}

	apiBody, err := json.Marshal(api)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}

	return shim.Success(apiBody)
}

func RequestAPI(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 5 {
		return shim.Error("参数个数不满足")
	}

	logID := args[0]
	reqID := args[1]
	apiID := args[2]
	apiArgs := args[3]
	apiTime := args[4]

	results, err := utils.GetStateByPartialCompositeKeys(stub, model.APIKey, []string{apiID})

	if err != nil {
		return shim.Error(fmt.Sprintf("不存在当前API:%s", err))
	}

	var api model.API
	err = json.Unmarshal(results[0], &api)
	if err != nil {
		return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
	}

	response, err := http.Post(api.URL, "application/json", bytes.NewBufferString(apiArgs))

	if err != nil {
		return shim.Error(fmt.Sprintf("HTTP请求失败:%s", err))
	}

	defer response.Body.Close()
	body, _ := ioutil.ReadAll(response.Body)

	log := &model.RequestAPILog{
		ID:      logID,
		ReqID:   reqID,
		APIID:   apiID,
		ReqHash: utils.GetSHA256String(args),
		ResHash: utils.GetSHA256String([]string{string(body)}),
		Time:    apiTime,
	}

	if err := utils.WriteLedger(log, stub, model.APIRequestKey, []string{log.ID}); err != nil {
		return shim.Error(fmt.Sprintf("写入API调用日志失败:%s", err))
	}

	return shim.Success(body)
}

// 查询API列表
func QueryAPIList(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	var apiList []model.API

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
			for _, item := range org.APIList {
				results, err := utils.GetStateByPartialCompositeKeys(stub, model.APIKey, []string{item})
				if err != nil {
					return shim.Error(fmt.Sprintf("%s", err))
				}
				var api model.API
				err = json.Unmarshal(results[0], &api)
				if err != nil {
					return shim.Error(fmt.Sprintf("反序列化出错: %s", err))
				}
				apiList = append(apiList, api)
			}
		}
	}

	apiListByte, err := json.Marshal(apiList)
	if err != nil {
		return shim.Error(fmt.Sprintf("序列化出错: %s", err))
	}
	return shim.Success(apiListByte)
}
