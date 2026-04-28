#!/bin/bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

# Configuration variables
REMOTE_USER="zead"
REMOTE_HOST="192.168.40.105"
REMOTE_DIR="~/identityhub-cx"
PROJECT_DIR="launcher/identityhub-cx"


# Function to build Identity Hub CX
build_java_projects() {
  echo "Building Identity Hub CX..."
  sed -i 's/\r$//' gradlew
  ./gradlew :launcher:identityhub-cx:clean :launcher:identityhub-cx:build
}

# Function to deploy to remote server
deploy_backend_to_remote() {
  echo "Deploying backend to remote server..."
  
  # Copy files to remote server
  scp -r -i "/home/zead/.ssh/pb40-server" "${PROJECT_DIR}/docker-compose.yaml"  "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}"
  scp -r -i "/home/zead/.ssh/pb40-server" "${PROJECT_DIR}/build/libs/"*.jar  "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/build/libs/"
  scp -r -i "/home/zead/.ssh/pb40-server" "${PROJECT_DIR}/Dockerfile"  "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/"
  scp -r -i "/home/zead/.ssh/pb40-server" "${PROJECT_DIR}/additional_config"  "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/"
  scp -r -i "/home/zead/.ssh/pb40-server" "${PROJECT_DIR}/main/resources/application.properties"  "${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/main/resources/"
}

run_docker_compose() {
  # SSH commands to run on remote server
  ssh -i "/home/zead/.ssh/pb40-server" $REMOTE_USER@$REMOTE_HOST << EOF
    cd $REMOTE_DIR
    echo "Starting new deployment..."
    docker compose build --no-cache
    docker compose up -d
    echo "Sleeping for 2 Min. before docker cleanup..."
    sleep 120
    docker system prune -f
    echo "Deployment complete!"
EOF
}


case "$1" in
  "all")
    echo "Executing all functions"
    build_java_projects
    deploy_backend_to_remote
    run_docker_compose
    ;;
  "build")
    echo "Executing build_java_projects only"
    build_java_projects
    ;;
  "deploy")
    echo "Executing deploy_backend_to_remote only"
    deploy_backend_to_remote
    ;;
  "docker")
    echo "Executing run_docker_compose only"
    run_docker_compose
    ;;
  *)
    echo "The script $0 requires one of the following parameter: [all|build|deploy|docker]"
    exit 1
    ;;
esac

echo "Build and deployment process completed successfully!"
