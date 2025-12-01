#!/bin/bash

# Banking Service Docker Quick Start Script
# This script helps you build and run the Banking Service with PostgreSQL

# TODO: This is yet to be tested.

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${GREEN}================================${NC}"
    echo -e "${GREEN}$1${NC}"
    echo -e "${GREEN}================================${NC}"
}

print_info() {
    echo -e "${YELLOW}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

show_menu() {
    print_header "Banking Service - Docker Quick Start"
    echo ""
    echo "Select an option:"
    echo "1. Build and run with Docker Compose (Recommended)"
    echo "2. Build Docker image only"
    echo "3. Stop and remove all containers"
    echo "4. View logs"
    echo "5. Run tests locally (without Docker)"
    echo "6. Exit"
    echo ""
    read -p "Enter your choice [1-6]: " choice
}

build_and_run() {
    print_header "Building and Running with Docker Compose"
    print_info "Starting PostgreSQL and Banking Service..."
    docker-compose up --build -d

    print_info "Waiting for services to be healthy..."
    sleep 10

    print_success "Services are running!"
    echo ""
    print_info "Application is available at: http://localhost:8080"
    print_info "Swagger UI: http://localhost:8080/swagger-ui.html"
    print_info "Health Check: http://localhost:8080/actuator/health"
    echo ""
    print_info "To view logs: docker-compose logs -f banking-service"
    print_info "To stop: docker-compose down"
}

build_image_only() {
    print_header "Building Docker Image"
    print_info "Building banking-service:1.0.0..."
    docker build -t banking-service:1.0.0 .
    print_success "Docker image built successfully!"
    echo ""
    print_info "To run the image with Docker Compose: ./docker-start.sh"
}

stop_containers() {
    print_header "Stopping and Removing Containers"
    print_info "Stopping Docker Compose services..."
    docker-compose down -v
    print_success "All services stopped and volumes removed!"
}

view_logs() {
    print_header "View Logs"
    print_info "Following logs from banking-service (Press Ctrl+C to exit)..."
    docker-compose logs -f banking-service
}

run_tests() {
    print_header "Running Tests"
    print_info "Running Maven tests..."
    mvn test
    print_success "Tests completed!"
}

while true; do
    show_menu

    case $choice in
        1)
            build_and_run
            ;;
        2)
            build_image_only
            ;;
        3)
            stop_containers
            ;;
        4)
            view_logs
            ;;
        5)
            run_tests
            ;;
        6)
            print_info "Exiting..."
            exit 0
            ;;
        *)
            print_error "Invalid choice. Please enter 1-6."
            ;;
    esac

    echo ""
    read -p "Press Enter to continue..."
done

