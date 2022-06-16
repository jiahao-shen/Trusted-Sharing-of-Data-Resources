package v1

import (
	bc "application/blockchain"
	"application/model"
	"application/pkg/app"
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

type CreateAPIBody struct {
	APIName         string `json:"apiName"`
	APIIntroduction string `json:"apiIntroduction"`
	APIAuthor       string `json:"apiAuthor"`
	APIURL          string `json:"apiURL"`
	APIType         string `json:"apiType"`
	APIRequest      string `json:"apiRequest"`
	APIResponse     string `json:"apiResponse"`
	APIVersion      string `json:"apiVersion"`
}

func CreateAPI(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(CreateAPIBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.APIName == "" || body.APIAuthor == "" || body.APIURL == "" || body.APIType == "" || body.APIRequest == "" || body.APIResponse == "" || body.APIVersion == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(body.APIName))
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(body.APIIntroduction))
	bodyBytes = append(bodyBytes, []byte(body.APIAuthor))
	bodyBytes = append(bodyBytes, []byte(body.APIURL))
	bodyBytes = append(bodyBytes, []byte(body.APIType))
	bodyBytes = append(bodyBytes, []byte(body.APIRequest))
	bodyBytes = append(bodyBytes, []byte(body.APIResponse))
	bodyBytes = append(bodyBytes, []byte(body.APIVersion))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("createAPI", bodyBytes)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	var data map[string]interface{}
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	appG.Response(http.StatusOK, "成功", data)
}

type RequestAPIBody struct {
	ReqID   string `json:"reqID"`
	APIID   string `json:"apiID"`
	APIArgs string `json:"apiArgs"`
}

func RequestAPI(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(RequestAPIBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.ReqID == "" || body.APIID == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(body.ReqID))
	bodyBytes = append(bodyBytes, []byte(body.APIID))
	bodyBytes = append(bodyBytes, []byte(body.APIArgs))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("requestAPI", bodyBytes)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	appG.Response(http.StatusOK, "成功", resp.Payload)
}

type QueryAPIListBody struct {
	OrganizationIDList []string `json:"organizationIDList"`
}

func QueryAPIList(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(QueryAPIListBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	var bodyBytes [][]byte
	for _, value := range body.OrganizationIDList {
		bodyBytes = append(bodyBytes, []byte(value))
	}

	resp, err := bc.ChannelExecute("queryAPIList", bodyBytes)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	var data []model.API
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	appG.Response(http.StatusOK, "成功", data)
}
