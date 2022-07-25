package main

import (
	"fmt"
	bc "sdk-go/blockchain"
)

func main() {
	bc.Init()

	resp, err := bc.ChannelExecute("invoke", [][]byte{[]byte("a"), []byte("b"), []byte("5")}, nil)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(string(resp.Payload))

	resp, err = bc.ChannelQuery("query", [][]byte{[]byte("a")}, nil)
	if err != nil {
		fmt.Println(err)
	}

	fmt.Println(string(resp.Payload))

}
