package main

import (
	"context"
	"fmt"
	"grpc/pb"
	"net"

	"google.golang.org/grpc"
)

// HelloService 定义我们的服务
type HelloService struct {
}

// SayHello 实现SayHello方法
func (s *HelloService) SayHello(ctx context.Context, req *pb.HelloRequest) (*pb.HelloResp, error) {
	return &pb.HelloResp{Message: "hello ,I'm codefish "}, nil
}

func main() {
	// 监听本地端口
	listener, err := net.Listen("tcp", ":12345")
	if err != nil {
		fmt.Println("net.Listen err: ", err)
	}
	// 新建gRPC服务器实例
	grpcServer := grpc.NewServer()
	// 在gRPC服务器注册我们的服务
	pb.RegisterHelloServer(grpcServer, &HelloService{})
	//用服务器 Serve() 方法以及我们的端口信息区实现阻塞等待，直到进程被杀死或者 Stop() 被调用
	err = grpcServer.Serve(listener)
	if err != nil {
		fmt.Println("grpcServer.Serve err: ", err)
	}
}
