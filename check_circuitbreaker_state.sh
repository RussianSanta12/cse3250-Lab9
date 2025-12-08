#!/bin/bash


URL="http://localhost:8080/actuator/circuitbreakers"
for i in {1..100}; do
  RESPONSE_TEST=$(curl -s http://localhost:8080/test)
  RESPONSE_CB=$(curl -s "$URL")
  # Extract the state for myServiceCB using grep/sed/awk (no jq assumed)
  STATE=$(echo "$RESPONSE_CB" | sed -n 's/.*"myServiceCB":{[^}]*"state":"\([A-Z_]*\)".*/\1/p')
  if [[ -z "$STATE" ]]; then
    STATE="UNKNOWN"
    echo "Request $i: Circuit Breaker State: $STATE (Raw response: $RESPONSE_CB) | Test response: $RESPONSE_TEST"
    sleep 0.1
    continue
  fi
  if [[ $RESPONSE_TEST == *"Success"* ]]; then
    RESULT="Success"
  else
    RESULT="Fallback"
  fi
  echo "Request $i: $RESULT | Circuit Breaker State: $STATE"
  sleep 0.1
done
