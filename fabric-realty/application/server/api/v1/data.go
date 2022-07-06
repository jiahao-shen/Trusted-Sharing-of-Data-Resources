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

type CreateDataBody struct {
	DataName         string `json:"dataName"`
	DataIntroduction string `json:"dataIntroduction"`
	DataAuthor       string `json:"dataAuthor"`
	DataField        string `json:"dataField"`
	DataType         string `json:"dataType"`
	DataShared       string `json:"dataShared"`
	DataClassified   string `json:"dataClassified"`
	DataVersion      string `json:"dataVersion"`
	DataLocation     string `json:"dataLocation"`
}

func CreateData(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(CreateDataBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.DataName == "" || body.DataAuthor == "" || body.DataField == "" || body.DataType == "" || body.DataShared == "" || body.DataClassified == "" || body.DataVersion == "" || body.DataLocation == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(body.DataName))
	bodyBytes = append(bodyBytes, []byte(body.DataIntroduction))
	bodyBytes = append(bodyBytes, []byte(body.DataAuthor))
	bodyBytes = append(bodyBytes, []byte(body.DataField))
	bodyBytes = append(bodyBytes, []byte(body.DataType))
	bodyBytes = append(bodyBytes, []byte(body.DataShared))
	bodyBytes = append(bodyBytes, []byte(body.DataClassified))
	bodyBytes = append(bodyBytes, []byte(body.DataVersion))
	bodyBytes = append(bodyBytes, []byte(body.DataLocation))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("createData", bodyBytes, nil)
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

type QueryDataListBody struct {
	OrganizationIDList []string `json:"organizationIDList"`
}

func QueryDataList(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(QueryDataListBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	var bodyBytes [][]byte
	for _, value := range body.OrganizationIDList {
		bodyBytes = append(bodyBytes, []byte(value))
	}

	resp, err := bc.ChannelExecute("queryDataList", bodyBytes, nil)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	var data []model.Data
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	appG.Response(http.StatusOK, "成功", data)
}
