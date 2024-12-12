

The `run-server.go` runs a HTTP server which prints HTTP config to the console, e.g. shared cipher suites, headers, etc. The program is in Go because the previously used Python lib can't show the "client hello" message for some reason. 

Install Go:

```bash
# assuming you're in /root
wget https://go.dev/dl/go1.23.4.linux-amd64.tar.gz  # from https://go.dev/dl/
tar -xf go1.23.4.linux-amd64.tar.gz
ln -s /root/go/bin/go /usr/local/bin/go
go version
```

Compile the server:

```bash
go mod init http-analyzer
go mod tidy
go build run-server.go
```

Additionally, you need to generate SSL certs. Select the defaults for everything. No config needed.

```bash
openssl req -x509 -newkey rsa:4096 -keyout server.key -out server.crt -days 365 -nodes
```

Run the server:

```bash
./run-server
2024/12/12 15:24:32 Starting server on :8443
```

Call it via an HTTP client or in your browser:

```bash
curl --insecure https://localhost:8443
```

Everything is printed into the console. Your browser might make multiple requests because it tries to get the `/favicon.ico`