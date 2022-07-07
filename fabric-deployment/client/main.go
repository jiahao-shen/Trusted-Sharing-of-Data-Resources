package main

import (
	bc "client/blockchain"
	"fmt"
)

func main() {
	bc.Init()

	resp, err := bc.ChannelExecute("queryAPIList", nil, []string{"peer0.AHospital.trustchain.com"})
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(resp.Payload)

	// if err = json.Unmarshal(bytes.NewBuffer(resp.Payload).Bytes(), &data); err != nil {
	// fmt.Println("反序列化失败")
	// }
}
