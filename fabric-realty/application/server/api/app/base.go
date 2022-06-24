package app

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

type Case1Request struct {
	Name     string `json:"name"`
	Identity string `json:"identity"`
	Medicine string `json:"medicine"`
}

// 场景一测试
func Case1(c *gin.Context) {
	appG := app.Gin{C: c}
	body := new(Case1Request)

	if err := c.ShouldBind(body); err != nil {
		appG.Response(http.StatusBadRequest, "失败", fmt.Sprintf("参数出错:%s", err))
		return
	}

	if body.Name == "" || body.Identity == "" || body.Medicine == "" {
		appG.Response(http.StatusBadRequest, "失败", "参数不能为空")
		return
	}

	var bodyBytes [][]byte
	bodyBytes = append(bodyBytes, []byte("org-hospital"))
	start := time.Now()
	resp, err := bc.ChannelExecute("queryAPIList", bodyBytes, nil)
	end := time.Since(start)
	fmt.Println("查询耗时:", end)

	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	var data []model.API
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	for _, v := range data {
		if v.Name == "挂号" {
			args := make(map[string]interface{})
			args["name"] = body.Name
			args["identity"] = body.Identity
			argstr, _ := json.Marshal(args)

			bodyBytes = [][]byte{}
			bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
			bodyBytes = append(bodyBytes, []byte("业务ID"))
			bodyBytes = append(bodyBytes, []byte(v.ID))
			bodyBytes = append(bodyBytes, []byte(string(argstr)))
			bodyBytes = append(bodyBytes, []byte(time.Now().String()))

			start = time.Now()
			resp, err = bc.ChannelExecute("requestAPI", bodyBytes, []string{"peer0.jd.com"})
			end = time.Since(start)
			fmt.Println("调用耗时:", end)
			if err != nil {
				appG.Response(http.StatusInternalServerError, "失败", err.Error())
				return
			}

			appG.Response(http.StatusOK, "成功", string(resp.Payload))
		}
	}

	bodyBytes = [][]byte{}
	bodyBytes = append(bodyBytes, []byte("org-pharmacy"))
	start = time.Now()
	resp, err = bc.ChannelExecute("queryAPIList", bodyBytes, nil)
	end = time.Since(start)
	fmt.Println("查询耗时:", end)
	if err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	data = []model.API{}
	if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
		appG.Response(http.StatusInternalServerError, "失败", err.Error())
		return
	}

	for _, v := range data {
		if v.Name == "查询处方" {
			args := make(map[string]interface{})
			args["identity"] = body.Identity
			args["medicine"] = body.Medicine
			argstr, _ := json.Marshal(args)

			bodyBytes = [][]byte{}
			bodyBytes = append(bodyBytes, []byte(uuid.New().String()))
			bodyBytes = append(bodyBytes, []byte("业务ID"))
			bodyBytes = append(bodyBytes, []byte(v.ID))
			bodyBytes = append(bodyBytes, []byte(string(argstr)))
			bodyBytes = append(bodyBytes, []byte(time.Now().String()))

			start = time.Now()
			resp, err = bc.ChannelExecute("requestAPI", bodyBytes, []string{"peer0.taobao.com"})
			end = time.Since(start)
			fmt.Println("调用耗时:", end)
			if err != nil {
				appG.Response(http.StatusInternalServerError, "失败", err.Error())
				return
			}

			appG.Response(http.StatusOK, "成功", string(resp.Payload))
		}
	}
}
