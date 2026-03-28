package render

import (
	"fmt"
	"os"
	"path/filepath"
	"strings"
	"text/template"

	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/files"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/profile"
	"github.com/Swofty-Developments/HypixelSkyBlock/setup/internal/spec"
)

func GenerateKubernetesAssets(p profile.Profile) error {
	if err := p.Validate(); err != nil {
		return err
	}
	if err := files.EnsureDir(filepath.Join(p.InstallDir, K8sDirName)); err != nil {
		return err
	}

	renderDir := filepath.Join(p.InstallDir, K8sDirName)
	filesToWrite := map[string]string{
		"namespace.yaml":    namespaceYAML(p),
		"configmap.yaml":    configMapYAML(p),
		"secret.yaml":       secretYAML(p),
		"proxy.yaml":        proxyYAML(p),
		"services.yaml":     servicesYAML(p),
		"game-servers.yaml": gameServersYAML(p),
		"autoscaling.yaml":  autoscalingYAML(p),
		"datastores.yaml":   datastoresYAML(p),
	}

	for name, content := range filesToWrite {
		if strings.TrimSpace(content) == "" {
			continue
		}
		if err := os.WriteFile(filepath.Join(renderDir, name), []byte(content), 0o644); err != nil {
			return err
		}
	}
	return os.WriteFile(filepath.Join(p.InstallDir, "README.generated.md"), []byte(KubernetesNotes(p)), 0o644)
}

func namespaceYAML(p profile.Profile) string {
	return fmt.Sprintf(`apiVersion: v1
kind: Namespace
metadata:
  name: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
`, p.KubernetesNamespace)
}

func configMapYAML(p profile.Profile) string {
	return fmt.Sprintf(`apiVersion: v1
kind: ConfigMap
metadata:
  name: hypixel-config
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
data:
  HYPIXEL_HOST_NAME: "0.0.0.0"
  HYPIXEL_ADVERTISED_PORT: "25565"
  HYPIXEL_MANAGEMENT_ENABLED: "true"
  HYPIXEL_MANAGEMENT_HOST_NAME: "0.0.0.0"
  HYPIXEL_MANAGEMENT_PORT: "9090"
`, p.KubernetesNamespace)
}

func secretYAML(p profile.Profile) string {
	mongoURI := p.MongoURI
	redisURI := p.RedisURI
	if p.InstallManagedDatastore {
		mongoURI = fmt.Sprintf("mongodb://mongodb.%s.svc.cluster.local:27017", p.KubernetesNamespace)
		redisURI = fmt.Sprintf("redis://redis.%s.svc.cluster.local:6379", p.KubernetesNamespace)
	}
	return fmt.Sprintf(`apiVersion: v1
kind: Secret
metadata:
  name: hypixel-secrets
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
type: Opaque
stringData:
  HYPIXEL_MONGODB: "%s"
  HYPIXEL_REDIS_URI: "%s"
  HYPIXEL_VELOCITY_SECRET: "%s"
  FORWARDING_SECRET: "%s"
`, p.KubernetesNamespace, mongoURI, redisURI, p.SharedSecret, p.SharedSecret)
}

func datastoresYAML(p profile.Profile) string {
	if !p.InstallManagedDatastore {
		return ""
	}
	storageClassLine := ""
	if strings.TrimSpace(p.StorageClassName) != "" {
		storageClassLine = fmt.Sprintf("  storageClassName: %q\n", p.StorageClassName)
	}
	return fmt.Sprintf(`apiVersion: v1
kind: Service
metadata:
  name: mongodb
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  selector:
    app: mongodb
  ports:
    - name: mongo
      port: 27017
      targetPort: mongo
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: mongodb
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  serviceName: mongodb
  replicas: 1
  selector:
    matchLabels:
      app: mongodb
  template:
    metadata:
      labels:
        app: mongodb
        app.kubernetes.io/managed-by: hypixel-setup
    spec:
      containers:
        - name: mongodb
          image: mongo:8.0.9
          ports:
            - name: mongo
              containerPort: 27017
          readinessProbe:
            tcpSocket:
              port: mongo
            initialDelaySeconds: 10
            periodSeconds: 5
          livenessProbe:
            tcpSocket:
              port: mongo
            initialDelaySeconds: 30
            periodSeconds: 10
          resources:
            requests:
              cpu: "250m"
              memory: "512Mi"
            limits:
              cpu: "2"
              memory: "4Gi"
          volumeMounts:
            - name: data
              mountPath: /data/db
  volumeClaimTemplates:
    - metadata:
        name: data
        labels:
          app.kubernetes.io/managed-by: hypixel-setup
      spec:
        accessModes: ["ReadWriteOnce"]
%s        resources:
          requests:
            storage: %s
---
apiVersion: v1
kind: Service
metadata:
  name: redis
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  selector:
    app: redis
  ports:
    - name: redis
      port: 6379
      targetPort: redis
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: redis
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  serviceName: redis
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
        app.kubernetes.io/managed-by: hypixel-setup
    spec:
      containers:
        - name: redis
          image: redis:7.4
          args: ["--appendonly", "yes"]
          ports:
            - name: redis
              containerPort: 6379
          readinessProbe:
            tcpSocket:
              port: redis
            initialDelaySeconds: 5
            periodSeconds: 5
          livenessProbe:
            tcpSocket:
              port: redis
            initialDelaySeconds: 20
            periodSeconds: 10
          resources:
            requests:
              cpu: "100m"
              memory: "256Mi"
            limits:
              cpu: "1"
              memory: "1Gi"
          volumeMounts:
            - name: data
              mountPath: /data
  volumeClaimTemplates:
    - metadata:
        name: data
        labels:
          app.kubernetes.io/managed-by: hypixel-setup
      spec:
        accessModes: ["ReadWriteOnce"]
%s        resources:
          requests:
            storage: %s
`, p.KubernetesNamespace, p.KubernetesNamespace, storageClassLine, p.MongoStorageSize, p.KubernetesNamespace, p.KubernetesNamespace, storageClassLine, p.RedisStorageSize)
}

func proxyYAML(p profile.Profile) string {
	return fmt.Sprintf(`apiVersion: apps/v1
kind: Deployment
metadata:
  name: hypixel-proxy
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  replicas: 2
  selector:
    matchLabels:
      app: hypixel-proxy
  template:
    metadata:
      labels:
        app: hypixel-proxy
        app.kubernetes.io/managed-by: hypixel-setup
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9090"
        prometheus.io/path: "/metrics"
    spec:
      topologySpreadConstraints:
        - maxSkew: 1
          topologyKey: kubernetes.io/hostname
          whenUnsatisfiable: ScheduleAnyway
          labelSelector:
            matchLabels:
              app: hypixel-proxy
      containers:
        - name: proxy
          image: %s
          imagePullPolicy: Never
          envFrom:
            - configMapRef:
                name: hypixel-config
            - secretRef:
                name: hypixel-secrets
          ports:
            - name: minecraft
              containerPort: 25565
            - name: management
              containerPort: 9090
          readinessProbe:
            httpGet:
              path: /readyz
              port: management
          livenessProbe:
            httpGet:
              path: /healthz
              port: management
          resources:
            requests:
              cpu: "500m"
              memory: "768Mi"
            limits:
              cpu: "2"
              memory: "2Gi"
---
apiVersion: v1
kind: Service
metadata:
  name: hypixel-proxy
  namespace: %s
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  type: %s
  selector:
    app: hypixel-proxy
  ports:
    - name: minecraft
      port: 25565
      targetPort: minecraft
    - name: management
      port: 9090
      targetPort: management
`, p.KubernetesNamespace, ImageRef("hypixel-proxy", p.ImageTag), p.KubernetesNamespace, p.ProxyServiceType)
}

func servicesYAML(p profile.Profile) string {
	parts := make([]string, 0, len(p.SelectedServices))
	for _, serviceName := range p.SelectedServices {
		svc := spec.ServiceByName(serviceName)
		parts = append(parts, execute(`apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Name }}
  namespace: {{ .Namespace }}
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  replicas: {{ .Replicas }}
  selector:
    matchLabels:
      app: {{ .Name }}
  template:
    metadata:
      labels:
        app: {{ .Name }}
        app.kubernetes.io/managed-by: hypixel-setup
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9090"
        prometheus.io/path: "/metrics"
    spec:
      topologySpreadConstraints:
        - maxSkew: 1
          topologyKey: kubernetes.io/hostname
          whenUnsatisfiable: ScheduleAnyway
          labelSelector:
            matchLabels:
              app: {{ .Name }}
      containers:
        - name: service
          image: {{ .Image }}
          imagePullPolicy: Never
          envFrom:
            - configMapRef:
                name: hypixel-config
            - secretRef:
                name: hypixel-secrets
          ports:
            - name: management
              containerPort: 9090
          readinessProbe:
            httpGet:
              path: /readyz
              port: management
          livenessProbe:
            httpGet:
              path: /healthz
              port: management
          resources:
            requests:
              cpu: {{ .RequestCPU }}
              memory: {{ .RequestMemory }}
            limits:
              cpu: {{ .LimitCPU }}
              memory: {{ .LimitMemory }}
`, map[string]any{
			"Name":          svc.DeploymentName,
			"Namespace":     p.KubernetesNamespace,
			"Replicas":      svc.Replicas,
			"Image":         ImageRef(svc.ImageName, p.ImageTag),
			"RequestCPU":    quote(svc.RequestCPU),
			"RequestMemory": quote(svc.RequestMemory),
			"LimitCPU":      quote(svc.LimitCPU),
			"LimitMemory":   quote(svc.LimitMemory),
		}))
	}
	return strings.Join(parts, "---\n")
}

func gameServersYAML(p profile.Profile) string {
	parts := make([]string, 0, len(p.SelectedServers))
	for _, serverType := range p.SelectedServers {
		server := spec.ServerByType(serverType)
		parts = append(parts, execute(`apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Name }}
  namespace: {{ .Namespace }}
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  replicas: {{ .Replicas }}
  selector:
    matchLabels:
      app: {{ .Name }}
      hypixel/server-type: {{ .ServerType }}
  template:
    metadata:
      labels:
        app: {{ .Name }}
        hypixel/server-type: {{ .ServerType }}
        app.kubernetes.io/managed-by: hypixel-setup
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "9090"
        prometheus.io/path: "/metrics"
    spec:
      topologySpreadConstraints:
        - maxSkew: 1
          topologyKey: kubernetes.io/hostname
          whenUnsatisfiable: ScheduleAnyway
          labelSelector:
            matchLabels:
              app: {{ .Name }}
      containers:
        - name: game-server
          image: {{ .Image }}
          imagePullPolicy: Never
          envFrom:
            - configMapRef:
                name: hypixel-config
            - secretRef:
                name: hypixel-secrets
          env:
            - name: HYPIXEL_ADVERTISED_HOST
              valueFrom:
                fieldRef:
                  fieldPath: status.podIP
            - name: SERVICE_CMD
              value: java $JAVA_OPTS -jar HypixelCore.jar {{ .ServerType }}
          ports:
            - name: minecraft
              containerPort: 25565
            - name: management
              containerPort: 9090
          readinessProbe:
            httpGet:
              path: /readyz
              port: management
          livenessProbe:
            httpGet:
              path: /healthz
              port: management
          resources:
            requests:
              cpu: {{ .RequestCPU }}
              memory: {{ .RequestMemory }}
            limits:
              cpu: {{ .LimitCPU }}
              memory: {{ .LimitMemory }}
`, map[string]any{
			"Name":          server.DeploymentName,
			"Namespace":     p.KubernetesNamespace,
			"Replicas":      server.Replicas,
			"ServerType":    serverType,
			"Image":         ImageRef("hypixel-game", p.ImageTag),
			"RequestCPU":    quote(server.RequestCPU),
			"RequestMemory": quote(server.RequestMemory),
			"LimitCPU":      quote(server.LimitCPU),
			"LimitMemory":   quote(server.LimitMemory),
		}))
	}
	return strings.Join(parts, "---\n")
}

func autoscalingYAML(p profile.Profile) string {
	if !p.EnableAutoscaling {
		return ""
	}
	parts := make([]string, 0, len(p.SelectedServers))
	for _, serverType := range p.SelectedServers {
		server := spec.ServerByType(serverType)
		metricName := strings.ToLower(serverType) + "_players"
		parts = append(parts, execute(`apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  name: {{ .Name }}
  namespace: {{ .Namespace }}
  labels:
    app.kubernetes.io/managed-by: hypixel-setup
spec:
  scaleTargetRef:
    name: {{ .Name }}
  minReplicaCount: {{ .MinReplicas }}
  maxReplicaCount: {{ .MaxReplicas }}
  cooldownPeriod: 60
  pollingInterval: 15
  triggers:
    - type: prometheus
      metadata:
        serverAddress: {{ .Prometheus }}
        metricName: {{ .MetricName }}
        threshold: "{{ .Threshold }}"
        query: sum(hypixel_proxy_server_type_players{server_type="{{ .ServerType }}"}) + {{ .Threshold }}
`, map[string]any{
			"Name":        server.DeploymentName,
			"Namespace":   p.KubernetesNamespace,
			"MinReplicas": server.MinReplicas,
			"MaxReplicas": server.MaxReplicas,
			"Prometheus":  p.PrometheusAddress,
			"MetricName":  metricName,
			"Threshold":   server.Threshold,
			"ServerType":  serverType,
		}))
	}
	return strings.Join(parts, "---\n")
}

func execute(tpl string, data any) string {
	var b strings.Builder
	template.Must(template.New("yaml").Parse(tpl)).Execute(&b, data)
	return b.String()
}

func quote(v string) string {
	return fmt.Sprintf("%q", v)
}
