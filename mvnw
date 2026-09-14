#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.2.0
#
# Optional ENV vars
# -----------------
#   JAVA_HOME - location of a JDK home dir, required when download maven via java source
#   MVNW_REPOURL - repo url base for downloading maven distribution
#   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
#   MVNW_VERBOSE - true: enable verbose log; debug: trace the mvnw script; others: silence the output
# ----------------------------------------------------------------------------

set -euf
[ "${MVNW_VERBOSE-}" != debug ] || set -x

# OS specific support.
native_path() { printf %s\\n "$1"; }
case "$(uname)" in
CYGWIN* | MINGW*)
  [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
  native_path() { cygpath --path --windows "$1"; }
  ;;
esac

# set JAVA_HOME to default if not set
if [ -z "${JAVA_HOME-}" ]; then
  if [ -x /usr/bin/java ]; then
    JAVA_HOME="$(/usr/bin/java -V 2>&1 | grep -o 'openjdk version "[0-9]*\.[0-9]*\.[0-9]*"' | head -1 | tr -d '"' | awk '{print $2}')"
  fi
fi

# Determine the project base dir
if [ -z "${MAVEN_PROJECTBASEDIR-}" ]; then
  MAVEN_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"
fi

# Maven distribution
MVN_REPO="${MVNW_REPOURL:-https://repo.maven.apache.org/maven2}"
MVN_DIST="apache-maven-3.9.6"
MVN_ZIP="${MVN_DIST}-bin.zip"
MVN_DIR="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/${MVN_DIST}"

# Maven wrapper jar
MVNW_JAR="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.jar"
MVNW_PROPERTIES="${MAVEN_PROJECTBASEDIR}/.mvn/wrapper/maven-wrapper.properties"
MVNW_URL="${MVNW_REPO}/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

# Verbose mode
if [ "${MVNW_VERBOSE-}" = true ] || [ "${MVNW_VERBOSE-}" = debug ]; then
  if [ -t 1 ]; then
    TERM_BOLD=$(tput bold 2>/dev/null || true)
    TERM_RESET=$(tput sgr0 2>/dev/null || true)
  fi
fi

# Use user MVNW_USERNAME/MVNW_PASSWORD if set
if [ -n "${MVNW_USERNAME-}" ] && [ -n "${MVNW_PASSWORD-}" ]; then
  MVNW_CURL_OPTS="${MVNW_CURL_OPTS-} -u ${MVNW_USERNAME}:${MVNW_PASSWORD}"
fi

# Process the arguments
while [ $# -gt 0 ]; do
  case $1 in
  -D* | -N | -npu)
    MVNW_ARGS="${MVNW_ARGS:-} $1"
    ;;
  --update-snapshots)
    MVNW_ARGS="${MVNW_ARGS:-} $1"
    ;;
  --fail-at-end)
    MVNW_ARGS="${MVNW_ARGS:-} $1"
    ;;
  -V | --version)
    if [ -f "${MVN_DIR}/bin/mvn" ]; then
      "${MVN_DIR}/bin/mvn" --version
    else
      echo "Apache Maven wrapper not downloaded yet"
    fi
    exit 0
    ;;
  --help)
    echo "Usage: ./mvnw [OPTIONS]"
    echo "  -Darg=value    Set Maven argument"
    echo "  --version      Show Maven version"
    echo "  --help         Show this help"
    exit 0
    ;;
  *)
    MVNW_ARGS="${MVNW_ARGS:-} $1"
    ;;
  esac
  shift
done

# Download Maven if not present
if [ ! -d "${MVN_DIR}" ]; then
  if [ "${MVNW_VERBOSE-}" = true ] || [ "${MVNW_VERBOSE-}" = debug ]; then
    echo "${TERM_BOLD:-}Downloading Maven ${MVN_DIST}...${TERM_RESET:-}"
  fi

  mkdir -p "${MVN_DIR}"

  # Download the distribution
  if command -v curl > /dev/null 2>&1; then
    curl ${MVNW_CURL_OPTS-} -fsSL "${MVN_REPO}/org/apache/maven/apache-maven/${MVN_DIST}/${MVN_ZIP}" -o "${MVN_DIR}/${MVN_ZIP}"
  elif command -v wget > /dev/null 2>&1; then
    wget ${MVNW_CURL_OPTS-} -q "${MVN_REPO}/org/apache/maven/apache-maven/${MVN_DIST}/${MVN_ZIP}" -O "${MVN_DIR}/${MVN_ZIP}"
  else
    echo "Error: Neither curl nor wget is available. Cannot download Maven." >&2
    exit 1
  fi

  # Extract the distribution
  unzip -q -o "${MVN_DIR}/${MVN_ZIP}" -d "${MVN_DIR}"
  rm -f "${MVN_DIR}/${MVN_ZIP}"

  if [ "${MVNW_VERBOSE-}" = true ] || [ "${MVNW_VERBOSE-}" = debug ]; then
    echo "${TERM_BOLD:-}Maven ${MVN_DIST} downloaded to: ${MVN_DIR}${TERM_RESET:-}"
  fi
fi

# Run Maven
exec "${MVN_DIR}/bin/mvn" ${MVNW_ARGS-}
