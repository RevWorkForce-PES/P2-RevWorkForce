# Jenkins CI Pipeline Documentation
### Project: RevWorkForce | Team Reference Guide

---

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Installing Jenkins](#installing-jenkins)
4. [Initial Jenkins Setup](#initial-jenkins-setup)
5. [Installing Required Plugins](#installing-required-plugins)
6. [Configuring JDK & Maven Tools](#configuring-jdk--maven-tools)
7. [The Jenkinsfile](#the-jenkinsfile)
8. [Creating the Pipeline Job](#creating-the-pipeline-job)
9. [Running the Pipeline](#running-the-pipeline)
10. [Viewing JaCoCo Coverage Report](#viewing-jacoco-coverage-report)
11. [Getting the JAR File](#getting-the-jar-file)
12. [Running the JAR File](#running-the-jar-file)
13. [Common Errors & Fixes](#common-errors--fixes)

---

## Overview

This document covers the complete CI (Continuous Integration) setup for the RevWorkForce project using:

- **Jenkins** → Automates build, test, and package stages
- **JaCoCo** → Generates code coverage reports
- **JUnit** → Displays test results inside Jenkins
- **Maven** → Builds and packages the Spring Boot application
- **GitHub** → Source code repository Jenkins pulls from

**Pipeline Flow:**
```
GitHub → Checkout → Build → Test → Code Coverage (JaCoCo) → Package → JAR Artifact
```

---

## Prerequisites

Before setting up Jenkins, make sure you have these installed:

| Tool | Version | Check Command |
|------|---------|---------------|
| Java JDK | 17 | `java -version` |
| Maven | 3.9.x | `mvn -version` |
| Git | Any | `git --version` |

---

## Installing Jenkins

### Option 1: WAR File (Easiest - No Installation)

1. Go to → **https://www.jenkins.io/download/**
2. Download the **Generic Java package (.war)** file
3. Open terminal/command prompt where the file was downloaded
4. Run:

**Windows:**
```cmd
java -jar jenkins.war --httpPort=8080
```

**Mac/Linux:**
```bash
java -jar jenkins.war --httpPort=8080
```

> Jenkins starts immediately — no installation needed!

### Option 2: Direct Install (Ubuntu/Linux)

```bash
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | \
  sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update && sudo apt install jenkins -y
sudo systemctl start jenkins
sudo systemctl enable jenkins
```

---

## Initial Jenkins Setup

1. Open browser → `http://localhost:8080`
2. Get the initial admin password:

**WAR file** → Password is printed in the terminal when Jenkins starts. Look for:
```
Jenkins initial setup is required.
Please use the following password: a1b2c3d4e5f6789...
```

**Direct install (Linux):**
```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

**Windows (default path):**
```
C:\Users\<yourname>\.jenkins\secrets\initialAdminPassword
```

3. Paste the password in browser
4. Click **"Install suggested plugins"** → wait ~3 minutes
5. Create your admin username & password
6. Click **Save and Finish → Start using Jenkins** ✅

---

## Installing Required Plugins

Go to **Manage Jenkins → Plugins → Available plugins**

Search and install each plugin:

| Plugin Name | Purpose in Pipeline |
|-------------|-------------------|
| `Pipeline` | Runs the Jenkinsfile |
| `Git` | Pulls code from GitHub |
| `Maven Integration` | Runs Maven commands |
| `JUnit` | Shows test results in Jenkins |
| `JaCoCo` | Shows code coverage report |
| `Code Coverage API` | Required by JaCoCo plugin |
| `Workspace Cleanup` | `cleanWs()` at end of pipeline |

After installing all → **Restart Jenkins**

---

## Configuring JDK & Maven Tools

> ⚠️ The names here MUST exactly match the names in the Jenkinsfile (`JDK-17` and `Maven-3.9`)

Go to **Manage Jenkins → Tools**

### JDK Configuration
```
Click "Add JDK"
  Name            →  JDK-17
  Uncheck "Install automatically"
  JAVA_HOME       →  (your java installation path)
```

Finding your JAVA_HOME:

**Windows (Command Prompt):**
```cmd
where java
echo %JAVA_HOME%
```
Typical path: `C:\Program Files\Java\jdk-17`

**Linux:**
```bash
readlink -f $(which java)
```
Typical path: `/usr/lib/jvm/java-17-openjdk-amd64`

**Mac:**
```bash
/usr/libexec/java_home -v 17
```

### Maven Configuration
```
Click "Add Maven"
  Name        →  Maven-3.9
  Uncheck "Install automatically"
  MAVEN_HOME  →  (your maven installation path)
```

Finding your Maven path:

**Windows:**
```cmd
where mvn
```

**Linux/Mac:**
```bash
which mvn
```

Click **Save** after configuring both.

---

## The Jenkinsfile

Save this file at the **root of your project** with the filename `Jenkinsfile` (no extension).

### For Windows:
```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                git branch: 'main', 
                    url: 'https://github.com/RevWorkForce-PES/P2-RevWorkForce.git'
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building...'
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Code Coverage - JaCoCo') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Packaging...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', 
                                     fingerprint: true
                }
            }
        }
    }
    
    post {
        success { echo 'Pipeline SUCCESS!' }
        failure { echo 'Pipeline FAILED!' }
        always { cleanWs() }
    }
}
```

### For Linux/Mac:
> Replace all `bat` with `sh` in the above file.

### Push Jenkinsfile to GitHub:
```bash
git add Jenkinsfile
git commit -m "Add Jenkins pipeline with JaCoCo"
git push origin main
```

---

## Required pom.xml Configuration

Make sure your `pom.xml` has the **JaCoCo plugin** and **Spring Boot plugin**:

```xml
<build>
    <plugins>
        <!-- Spring Boot - makes runnable JAR -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>

        <!-- JaCoCo - code coverage -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.11</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

## Creating the Pipeline Job

1. Jenkins Dashboard → **New Item**
2. Enter name: `JenkinsPipeline` (or any name)
3. Select **Pipeline** → Click **OK**
4. Scroll to **Pipeline section** and fill in:

```
Definition      →  Pipeline script from SCM
SCM             →  Git
Repository URL  →  https://github.com/RevWorkForce-PES/P2-RevWorkForce.git
Branch          →  */main
Script Path     →  Jenkinsfile
```

5. Click **Save**

---

## Running the Pipeline

1. Click **"Build Now"** on the left sidebar
2. A build appears under **Build History** → click it (e.g. `#1`)
3. Click **"Console Output"** to watch live logs

### Expected Successful Output:
```
✅ Checkout              - Code pulled from GitHub
✅ Build                 - mvn clean compile passed
✅ Test                  - All JUnit tests passed
✅ Code Coverage-JaCoCo  - Coverage report generated
✅ Package               - JAR file created

Finished: SUCCESS
```

---

## Viewing JaCoCo Coverage Report

After a successful build, on the build page look at the **left sidebar:**

```
Coverage Report   ← Click this
```

You will see a breakdown like:

| Metric | Coverage |
|--------|----------|
| Line Coverage | 85% |
| Branch Coverage | 76% |
| Method Coverage | 90% |
| Class Coverage | 95% |

You can also drill down into individual classes to see which lines are covered (green) and which are not (red).

---

## Getting the JAR File

### Option 1: From Jenkins UI
On the build page → click **"Build Artifacts"** in the left sidebar → download the `.jar` file directly.

### Option 2: From File System
```
C:\Users\<yourname>\.jenkins\workspace\JenkinsPipeline\target\
```
Look for:
```
revworkforce-0.0.1-SNAPSHOT.jar
```

---

## Running the JAR File

1. Open Command Prompt in the `target` folder
2. Run:

```cmd
java -jar revworkforce-0.0.1-SNAPSHOT.jar
```

3. You'll see Spring Boot startup logs:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 :: Spring Boot ::

Started RevWorkForceApplication in 3.456 seconds ✅
```

4. Open browser → `http://localhost:8080`

---

## Common Errors & Fixes

### ❌ `Tool type "maven" does not have an install of "Maven-3.9" configured`
**Fix:** Go to **Manage Jenkins → Tools** and add Maven with name exactly `Maven-3.9`

---

### ❌ `Cannot run program "sh": CreateProcess error=2`
**Fix:** You're on Windows. Replace all `sh` with `bat` in your Jenkinsfile.

---

### ❌ `Tool type "jdk" does not have an install of "JDK-17" configured`
**Fix:** Go to **Manage Jenkins → Tools** and add JDK with name exactly `JDK-17`

---

### ❌ `no main manifest attribute` when running JAR
**Fix:** Add `spring-boot-maven-plugin` to your `pom.xml` (see pom.xml section above)

---

### ❌ JaCoCo report not showing
**Fix:** Make sure `jacoco-maven-plugin` is in `pom.xml` and `JaCoCo` + `Code Coverage API` plugins are installed in Jenkins

---

### ❌ Build fails at Test stage
**Fix:** Check that your test files exist at `src/test/java` and `pom.xml` has correct test dependencies

---

## Quick Reference Cheat Sheet

| Task | Where |
|------|-------|
| Start Jenkins | `java -jar jenkins.war --httpPort=8080` |
| Open Jenkins | `http://localhost:8080` |
| Install Plugins | Manage Jenkins → Plugins → Available |
| Configure Tools | Manage Jenkins → Tools |
| Create Job | New Item → Pipeline |
| Run Pipeline | Pipeline page → Build Now |
| View Logs | Build → Console Output |
| View Coverage | Build → Coverage Report |
| Download JAR | Build → Build Artifacts |
| JAR Location | `...\.jenkins\workspace\<jobname>\target\` |
| Run JAR | `java -jar filename.jar` |

---

*Documentation created for RevWorkForce Team — P2 Project*
