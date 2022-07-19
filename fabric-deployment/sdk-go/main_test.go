package main

import (
	"log"
	"testing"

	"google.golang.org/grpc"
)

func Test_GRPC_Client(t *testing.T) {
	conn, err := grpc.Dial("192.168.8.225:7051", grpc.WithInsecure())
	if err != nil {
		log.Fatalf("grpc.Dial err: %v", err)
	}
	defer conn.Close()

	// client := pb.NewSearchServiceClient(conn)
	// resp, err := client.Search(context.Background(), &pb.SearchRequest{
	// 	Request: "gRPC",
	// })
	// if err != nil {
	// 	log.Fatalf("client.Search err: %v", err)
	// }

	// log.Printf("resp: %s", resp.GetResponse())
}
