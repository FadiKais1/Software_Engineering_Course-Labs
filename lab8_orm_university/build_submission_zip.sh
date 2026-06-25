#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -ne 1 ]; then
  echo "Usage: ./build_submission_zip.sh ID1_ID2"
  echo "Example: ./build_submission_zip.sh 123456789_987654321"
  exit 1
fi

SUBMISSION_NAME="$1"

mvn clean package
mkdir -p submission
cp target/lab8-orm-university.jar submission/lab8-orm-university.jar
cp Lab8_Report.pdf submission/Lab8_Report.pdf
(cd submission && zip -r "../${SUBMISSION_NAME}.zip" Lab8_Report.pdf lab8-orm-university.jar)

echo "Created ${SUBMISSION_NAME}.zip"
