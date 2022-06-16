package main

import (
	"encoding/json"
	"fmt"
	"html"

	// "io/ioutil"
	"net/http"
	"time"
)

func index(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello, %q", html.EscapeString(r.URL.Path))
}

type testRequest struct {
	IdentityCard string `json:"identityCard"`
}

type testResponse struct {
	NATTResult string `json:"nattResult"`
}

func test(w http.ResponseWriter, r *http.Request) {
	fmt.Println(time.Now())

	// body, _ := ioutil.ReadAll(r.Body)

	// var request testRequest
	// err := json.Unmarshal(body, &request)
	// if err != nil {
	// 	fmt.Println("反序列化出错:", err)
	// }
	// fmt.Println(request)

	response := &testResponse{
		NATTResult: "阴性",
	}

	msg, err := json.Marshal(response)
	if err != nil {
		fmt.Println("序列化出错:", err)
	}

	w.WriteHeader(200)
	w.Write(msg)
}

func main() {
	http.HandleFunc("/", index)
	http.HandleFunc("/test", test)

	if err := http.ListenAndServe("0.0.0.0:8080", nil); err != nil {
		fmt.Println("ListenAndServe: ", err)
	}
}
