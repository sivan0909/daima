@echo off
set PORT=18088
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%PORT%') do (
  taskkill /F /PID %%a >nul 2>&1
)
mvn spring-boot:run