#!/bin/bash
set -euo pipefail

APPS=("BookingService" "HotelService", "Gateway", "DiscoveryService", "UserService")

for app in "${APPS[@]}"; do
  echo "Starting $app..."
  (
    cd "$app" || exit 1
    ./mvnw spring-boot:run &
  )
done

echo "All apps started. Press Ctrl+C to stop."
wait