package main

import (
	"context"
	"fmt"
	"grpc/pb"

	"google.golang.org/grpc"
)

func main() {
	// 连接服务器
	conn, err := grpc.Dial(":12345", grpc.WithInsecure())
	if err != nil {
		fmt.Println("net.Connect err: ", err)
	}
	defer conn.Close()

	// 建立gRPC连接
	grpcClient := pb.NewHelloClient(conn)
	// 创建发送结构体
	req := pb.HelloRequest{
		Name: "grpc",
	}
	// 调用我们的服务(SayHello方法)
	// 同时传入了一个 context.Context ，在有需要时可以让我们改变RPC的行为，比如超时/取消一个正在运行的RPC
	res, err := grpcClient.SayHello(context.Background(), &req)
	if err != nil {
		fmt.Println("Call SayHello err: ", err)
	}
	// 打印返回值
	fmt.Println(res)
}
