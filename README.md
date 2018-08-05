# s3 proxy test with Netty

s3 requests proxy based on Netty stack    

## Usage

Set env vars

```
-DS3_HOST=localhost - RadosGW (or S3 backend) hostname or IP
-DS3_PORT=8080      - RadosGW (or S3 backend) port
-DPROXY_PORT=8000   - port to listen on
```

sbt run