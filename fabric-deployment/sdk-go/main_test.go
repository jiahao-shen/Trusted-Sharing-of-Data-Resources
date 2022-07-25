package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	bc "sdk-go/blockchain"
	"sdk-go/model"
	"testing"
	"time"

	"github.com/google/uuid"
)

func PrettyPrint(str string) string {
	var prettyJSON bytes.Buffer
	if err := json.Indent(&prettyJSON, []byte(str), "", "    "); err != nil {
		panic(err)
	}
	return prettyJSON.String()
}

func Test_CreateOrganization(t *testing.T) {
	bc.Init()

	// orgName := "北京市第三人民医院"
	// orgType := "医院"
	// orgIntroduction := "始建于1958年，是国家卫生健康委委管的集医疗、教学、科研和预防保健为一体的现代化综合性三级甲等医院。"
	// orgSuperiorID := ""

	orgName := "北京市同仁堂药店"
	orgType := "中医药店"
	orgIntroduction := "同仁堂药店创始于清·康熙八年(公元1669年)，是驰名中外的老字号，也是全国最大的药店之一，至今已有330多年的历史，早在清· 雍正元年就供奉御药，独办御药至清末近200年。"
	orgSuperiorID := ""

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(orgName))
	bodyBytes = append(bodyBytes, []byte(orgType))
	bodyBytes = append(bodyBytes, []byte(orgIntroduction))
	bodyBytes = append(bodyBytes, []byte(orgSuperiorID))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("createOrganization", bodyBytes, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(string(resp.Payload))
}

func Test_QueryOrganizationList(t *testing.T) {
	bc.Init()

	resp, err := bc.ChannelQuery("queryOrganizationList", nil, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(PrettyPrint(string(resp.Payload)))

}

func Test_CreateAPI(t *testing.T) {
	bc.Init()

	// apiName := "挂号"
	// apiIntroduction := "北京市第三人民医院挂号服务"
	// apiAuthor := "6bb32a62-47b4-4652-971f-66e9638e9780"
	// apiURL := "http://192.168.8.225:8000/api/app/registration"
	// apiType := "POST"
	// apiRequest := "[{\"field\":\"name\",\"type\":\"string\"},{\"field\":\"identity\",\"type\":\"string\"}]"
	// apiResponse := "{}"
	// apiVersion := "v1.0.0"

	apiName := "查询处方"
	apiIntroduction := "查询开药信息"
	apiAuthor := "e2cff094-369f-415e-b43f-a9e287778ce2"
	apiURL := "http://192.168.8.225:8000/api/app/queryPrescription"
	apiType := "POST"
	apiRequest := "[{\"field\":\"identity\",\"type\":\"string\"},{\"field\":\"medicine\",\"type\":\"string\"}]"
	apiResponse := "{}"
	apiVersion := "v1.0.0"

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(apiName))
	bodyBytes = append(bodyBytes, []byte(apiIntroduction))
	bodyBytes = append(bodyBytes, []byte(apiAuthor))
	bodyBytes = append(bodyBytes, []byte(apiURL))
	bodyBytes = append(bodyBytes, []byte(apiType))
	bodyBytes = append(bodyBytes, []byte(apiRequest))
	bodyBytes = append(bodyBytes, []byte(apiResponse))
	bodyBytes = append(bodyBytes, []byte(apiVersion))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("createAPI", bodyBytes, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(string(resp.Payload))
}

func Test_QueryAPIList(t *testing.T) {
	bc.Init()

	resp, err := bc.ChannelQuery("queryAPIList", nil, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(PrettyPrint(string(resp.Payload)))
}

func Test_Case1(t *testing.T) {
	bc.Init()

	// 查医院目录链
	resp, err := bc.ChannelQuery("queryAPIList", [][]byte{[]byte("6bb32a62-47b4-4652-971f-66e9638e9780")}, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(PrettyPrint(string(resp.Payload)))

	var data []model.API
	if err := json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		fmt.Println(err)
	}

	// 挂号
	for _, v := range data {
		if v.Name == "挂号" {
			args := make(map[string]interface{})
			args["name"] = "沈嘉浩"
			args["identity"] = "320506199706024014"
			argstr, _ := json.Marshal(args)

			var bodyBytes [][]byte
			bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
			bodyBytes = append(bodyBytes, []byte("HospitalB-SDK"))
			bodyBytes = append(bodyBytes, []byte(v.ID))
			bodyBytes = append(bodyBytes, []byte(string(argstr)))
			bodyBytes = append(bodyBytes, []byte(time.Now().String()))

			resp, err := bc.ChannelExecute("requestAPI", bodyBytes, nil)
			if err != nil {
				fmt.Println(err)
			}

			fmt.Println(PrettyPrint(string(resp.Payload)))
		}
	}

	// 查药店目录链
	resp, err = bc.ChannelQuery("queryAPIList", [][]byte{[]byte("e2cff094-369f-415e-b43f-a9e287778ce2")}, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(PrettyPrint(string(resp.Payload)))

	data = []model.API{}
	if err := json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		fmt.Println(err)
	}

	// 查处方
	for _, v := range data {
		if v.Name == "查询处方" {
			args := make(map[string]interface{})
			args["identity"] = "320506199706024014"
			args["medicine"] = "盘尼西林"
			argstr, _ := json.Marshal(args)

			var bodyBytes [][]byte
			bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
			bodyBytes = append(bodyBytes, []byte("HospitalB-SDK"))
			bodyBytes = append(bodyBytes, []byte(v.ID))
			bodyBytes = append(bodyBytes, []byte(string(argstr)))
			bodyBytes = append(bodyBytes, []byte(time.Now().String()))

			resp, err := bc.ChannelExecute("requestAPI", bodyBytes, nil)
			if err != nil {
				fmt.Println(err)
			}

			fmt.Println(PrettyPrint(string(resp.Payload)))
		}
	}

}
