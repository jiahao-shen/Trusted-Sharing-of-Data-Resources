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

type CreateOrganizationBody struct {
	OrganizationName         string `json:"organizationName"`
	OrganizationType         string `json:"organizationType"`
	OrganizationIntroduction string `json:"organizationIntroduction"`
	OrganizationSuperiorID   string `json:"organizationSuperiorID"`
}

func CreateOrganization(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(CreateOrganizationBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.OrganizationName == "" || body.OrganizationType == "" || body.OrganizationIntroduction == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
	bodyBytes = append(bodyBytes, []byte(body.OrganizationName))
	bodyBytes = append(bodyBytes, []byte(body.OrganizationType))
	bodyBytes = append(bodyBytes, []byte(body.OrganizationIntroduction))
	bodyBytes = append(bodyBytes, []byte(body.OrganizationSuperiorID))
	bodyBytes = append(bodyBytes, []byte(time.Now().String()))

	resp, err := bc.ChannelExecute("createOrganization", bodyBytes, nil)
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

type QueryOrganizationBody struct {
	OrganizationIDList []string `json:"organizationIDList"`
}

func QueryOrganizationList(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(QueryOrganizationBody)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	var bodyBytes [][]byte
	for _, value := range body.OrganizationIDList {
		bodyBytes = append(bodyBytes, []byte(value))
	}

	resp, err := bc.ChannelExecute("queryOrganizationList", bodyBytes, nil)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	var data []model.Organization
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	appG.Response(http.StatusOK, "成功", data)
}
