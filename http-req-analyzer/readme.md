{:toc}

# Running the server



:red_circle: Obsolete. Use https://tls.peet.ws/api/all - it's the endpoint which they use in https://github.com/bogdanfinn/tls-client/ for testing



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

You can also run a proxy on the same host:

```bash
curl -v --insecure -x 'http://klaus:123456@5.161.94.81:3128' 'https://5.161.94.81:8443'
```



# Understanding the logs

The logs will show the client hello message of each HTTP request.

> **The 'client hello' message:** The client initiates the handshake by sending a "hello" message to the server. The message will include which TLS version the client supports, the cipher suites supported, and a string of random bytes known as the "client random."

Request from curl:

```yaml
=== Client Hello Details ===							  === HTTP Request Details ===
Server Name (SNI): 										  Method: GET
														  URL: /
Supported TLS Versions:									  Protocol: HTTP/2.0
  - TLS 1.3 (0x0304)									  Remote Address: 5.161.94.81:56686
  - TLS 1.2 (0x0303)
														  Headers:
Advertised Cipher Suites:								    User-Agent: curl/8.5.0
  - TLS_AES_256_GCM_SHA384 (0x1302)						    Accept: */*
  - TLS_CHACHA20_POLY1305_SHA256 (0x1303)
  - TLS_AES_128_GCM_SHA256 (0x1301)						  TLS Connection Details:
  - TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 (0xC02C)	    TLS Version: 304
...														    Cipher Suite: 1301
														    Server Name (SNI):
Supports ALPN: true
ALPN Protocols: [h2 http/1.1]
=== End Client Hello Details ===
```

Request from Chrome:

```yaml
=== Client Hello Details ===                            === HTTP Request Details ===
Server Name (SNI):                                      Method: GET
                                                        URL: /
Supported TLS Versions:                                 Protocol: HTTP/2.0
  - GREASE (0x3a3a) (0x3A3A)                            Remote Address: 62.143.229.221:60200
  - TLS 1.3 (0x0304)
  - TLS 1.2 (0x0303)                                    Headers:
                                                          Sec-Ch-Ua-Mobile: ?0
Advertised Cipher Suites:                                 Accept-Language: en-US,en
  - GREASE (0x1a1a) (0x1A1A)                              Sec-Fetch-User: ?1
  - TLS_AES_128_GCM_SHA256 (0x1301)                       Priority: u=0, i
  - TLS_AES_256_GCM_SHA384 (0x1302)                       Cache-Control: max-age=0
  - TLS_CHACHA20_POLY1305_SHA256 (0x1303)				  ...	
...                                                     TLS Connection Details:
                                                          TLS Version: 304
Supports ALPN: true                                       Cipher Suite: 1301
ALPN Protocols: [h2 http/1.1]                             Server Name (SNI):
=== End Client Hello Details ===
```

Request from OkHttp (with manipulated headers):

```yaml
=== Client Hello Details ===                              === HTTP Request Details ===
Server Name (SNI):                                        Method: GET
                                                          URL: /
Supported TLS Versions:                                   Protocol: HTTP/2.0
 - TLS 1.3 (0x0304)                                       Remote Address: 5.161.94.81:34278
 - TLS 1.2 (0x0303)
                                                          Headers:
Advertised Cipher Suites:                                    Sec-Ch-Ua-Mobile: ?0
 - TLS_AES_128_GCM_SHA256 (0x1301)                           Accept-Encoding: gzip
 - TLS_AES_256_GCM_SHA384 (0x1302)                           Accept: text/html,...,*/*;q=0.8
 - TLS_CHACHA20_POLY1305_SHA256 (0x1303)                     Sec-Ch-Ua: "Chromium";v="116", ... "Google Chrome";v="116"
 - TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256 (0xC02B)          Sec-Ch-Ua-Platform: Windows
 - TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 (0xC02F)            Sec-Fetch-Dest: document
 - TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384 (0xC02C)          Sec-Fetch-Mode: navigate
 - TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 (0xC030)            Sec-Fetch-Site: cross-site
 - TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256 (0xCCA9)    Sec-Fetch-User: ?1
 - TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256 (0xCCA8)      Upgrade-Insecure-Requests: 1
 - TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA (0xC013)               Accept-Language: en-US,en;q=0.9
 - TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA (0xC014)               Referer: https://www.google.com/
 - TLS_RSA_WITH_AES_128_GCM_SHA256 (0x009C)                  User-Agent: Mozilla/5.0 ... Chrome/116.0.0.0 Safari/537.36
 - TLS_RSA_WITH_AES_256_GCM_SHA384 (0x009D)
 - TLS_RSA_WITH_AES_128_CBC_SHA (0x002F)                  TLS Connection Details:
 - TLS_RSA_WITH_AES_256_CBC_SHA (0x0035)                     TLS Version: 304
                                                             Cipher Suite: 1301
Supports ALPN: true                                          Server Name (SNI):
ALPN Protocols: [h2 http/1.1]
=== End Client Hello Details ===
```

The client hello and headers of curl and Chrome are widely different, i.e. detectable. Even OkHttp is detectable, although all proper cipher suites match, because it doesn't use GREASE. So your goal should be to align your HTTP request as much as possible to the one of a browser. Either via proxy or directly in your HTTP client. There are libraries like `tls-client` (available in Go and Python), which do it your you.
