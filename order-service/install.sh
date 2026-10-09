#!/bin/bash

set -e

echo "======================================"
echo "Order Service - EC2 Bootstrap"
echo "======================================"

# Atualizar sistema
apt-get update -y
apt-get upgrade -y

# Instalar ferramentas
apt-get install -y \
    git \
    curl \
    wget \
    unzip \
    ca-certificates

# Instalar JDK 21
apt-get install -y openjdk-21-jdk

# Instalar Maven
apt-get install -y maven

# Verificar instalações
echo "Java:"
java -version

echo "Maven:"
mvn -version

echo "Git:"
git --version

# Criar diretório da aplicação
mkdir -p /opt/order-service

# Clonar projeto
cd /opt/order-service

git clone https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git .

# Garantir permissões
chown -R ubuntu:ubuntu /opt/order-service

echo "======================================"
echo "Bootstrap concluído!"
echo "======================================"