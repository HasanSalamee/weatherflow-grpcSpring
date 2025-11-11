package main

import (
    "fmt"

    "github.com/hasan/weather"
    "google.golang.org/protobuf/encoding/protojson"
)

func main() {
    req := &weather.ReportRequest{Region: "north-region"}
    marshaler := protojson.MarshalOptions{Multiline: true, Indent: "  ", UseProtoNames: true}
    out, err := marshaler.Marshal(req)
    if err != nil {
        panic(err)
    }
    fmt.Println("ReportRequest as JSON:\n", string(out))
}
