{{- define "msvc-chart.defaultName" -}}
{{- printf "ms-%s" .Release.Name -}}
{{- end -}}

{{- define "msvc-chart.deployment.name" -}}
{{- default (include "msvc-chart.defaultName" .) .Values.deploymentName | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "msvc-chart.service.name" -}}
{{- default (include "msvc-chart.defaultName" .) .Values.serviceName | trunc 63 | trimSuffix "-" -}}
{{- end -}}


{{- define "msvc-chart.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}


