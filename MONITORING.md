# Monitoring Setup - Grafana & Prometheus

This document explains how to monitor the loan-tech-api application using Grafana and Prometheus.

## Components

1. **Spring Boot Actuator** - Exposes application metrics
2. **Prometheus** - Scrapes and stores metrics
3. **Loki** - Collects and stores logs
4. **Grafana** - Visualizes metrics and logs

## Prerequisites

Your docker-compose.yml should include:
- Prometheus (port 9090)
- Loki (port 3100)
- Grafana (port 3000)

## Configuration

### 1. Spring Boot Application

The application is configured to:
- Expose metrics at: `http://localhost:9091/actuator/prometheus`
- Send logs to Loki at: `http://loki:3100`

**Endpoints exposed:**
- Health: `http://localhost:9091/actuator/health`
- Metrics: `http://localhost:9091/actuator/metrics`
- Prometheus: `http://localhost:9091/actuator/prometheus`

### 2. Prometheus Configuration

Mount the `prometheus.yml` file in your docker-compose:

```yaml
prometheus:
  image: prom/prometheus:latest
  ports:
    - "9090:9090"
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
```

### 3. Loki Configuration

Add Loki to docker-compose:

```yaml
loki:
  image: grafana/loki:latest
  ports:
    - "3100:3100"
  command: -config.file=/etc/loki/local-config.yaml
```

### 4. Grafana Configuration

Add Grafana to docker-compose:

```yaml
grafana:
  image: grafana/grafana:latest
  ports:
    - "3000:3000"
  environment:
    - GF_SECURITY_ADMIN_PASSWORD=admin
  volumes:
    - grafana-storage:/var/lib/grafana
```

## Setup Steps

### 1. Start Services

```bash
docker-compose up -d
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 2. Configure Grafana Data Sources

Access Grafana at `http://localhost:3000` (admin/admin)

**Add Prometheus Data Source:**
1. Go to Configuration → Data Sources
2. Click "Add data source"
3. Select "Prometheus"
4. URL: `http://prometheus:9090`
5. Click "Save & Test"

**Add Loki Data Source:**
1. Go to Configuration → Data Sources
2. Click "Add data source"
3. Select "Loki"
4. URL: `http://loki:3100`
5. Click "Save & Test"

### 3. Import Dashboards

**For Spring Boot Metrics:**
1. Go to Dashboards → Import
2. Enter dashboard ID: **4701** (JVM Micrometer)
3. Select Prometheus data source
4. Click Import

**For Application Logs:**
1. Go to Explore
2. Select Loki data source
3. Use LogQL query: `{application="loan-tech-api"}`

### 4. Create Custom Dashboard

Create panels for:
- HTTP request rates
- Response times
- Database connection pool metrics
- JVM memory usage
- Error rates
- Custom business metrics

## Useful Queries

### Prometheus (PromQL)

```promql
# HTTP Request Rate
rate(http_server_requests_seconds_count{application="loan-tech-api"}[5m])

# Average Response Time
rate(http_server_requests_seconds_sum{application="loan-tech-api"}[5m]) /
rate(http_server_requests_seconds_count{application="loan-tech-api"}[5m])

# JVM Memory Usage
jvm_memory_used_bytes{application="loan-tech-api", area="heap"}

# Database Connection Pool
hikaricp_connections_active{application="loan-tech-api"}

# Error Rate
rate(http_server_requests_seconds_count{application="loan-tech-api", status="500"}[5m])
```

### Loki (LogQL)

```logql
# All logs for the application
{application="loan-tech-api"}

# Error logs only
{application="loan-tech-api"} |= "ERROR"

# Logs for specific logger
{application="loan-tech-api"} | json | logger_name="com.adme.learning.loan.tech.api"

# Count of error logs over time
sum(count_over_time({application="loan-tech-api"} |= "ERROR" [1m]))
```

## Verification

1. **Check Prometheus targets:**
   - Go to `http://localhost:9090/targets`
   - Verify loan-tech-api is UP

2. **Check metrics endpoint:**
   - Visit `http://localhost:9091/actuator/prometheus`
   - Should see metrics in Prometheus format

3. **Check logs in Grafana:**
   - Go to Explore → Loki
   - Query: `{application="loan-tech-api"}`
   - Should see application logs

## Troubleshooting

### Application not appearing in Prometheus
- Check prometheus.yml configuration
- Verify target URL is correct (host.docker.internal on Mac/Windows, or container name)
- Restart Prometheus after config changes

### No logs in Loki
- Check Loki URL in logback-spring.xml
- Verify Loki is running: `docker ps | grep loki`
- Check application logs for Loki connection errors

### Grafana can't connect to data sources
- Verify services are on same Docker network
- Use container names as hostnames (prometheus, loki)
- Check firewall rules
