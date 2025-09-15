# deploy-backend.ps1
param(
    [string]$Server       = "159.223.202.97",
    [string]$User         = "root",
    [string]$RemoteDir    = "/moroka",             # where the jar lives on the server
    [string]$ServiceName  = "springboot-backend",  # systemd service name
    [string]$JarName      = "springboot-backend.jar",
    [string]$Owner        = "root:root"            # chown owner:group after upload
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command pscp  -ErrorAction SilentlyContinue)) { throw "pscp.exe not found. Install PuTTY and add it to PATH." }
if (-not (Get-Command plink -ErrorAction SilentlyContinue)) { throw "plink.exe not found. Install PuTTY and add it to PATH." }

$repo = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $repo

# Try to build the JAR (Maven or Gradle), then resolve the artifact path
$jar = Join-Path $repo ("target\" + $JarName)
$built = $false
if (Test-Path .\mvnw.cmd)               { & .\mvnw.cmd -DskipTests clean package; $built = $true }
elseif (Get-Command mvn -ErrorAction SilentlyContinue) { & mvn -DskipTests clean package; $built = $true }
elseif (Test-Path .\gradlew.bat)        { & .\gradlew.bat -x test bootJar; $built = $true }
elseif (Get-Command gradle -ErrorAction SilentlyContinue) { & gradle -x test bootJar; $built = $true }

if (-not (Test-Path $jar)) {
    # Fallback: pick the newest non-plain jar from target/
    $candidate = Get-ChildItem -Path (Join-Path $repo "target") -Filter *.jar -File -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -notlike "*plain.jar" } |
            Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($candidate) { $jar = $candidate.FullName }
}
if (-not (Test-Path $jar)) { throw "JAR not found (target\$JarName or latest in target/)." }
Write-Host "Using JAR: $jar" -ForegroundColor Green

# Password prompt
$sec  = Read-Host -AsSecureString "SSH password for $User@$Server"
$BSTR = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($sec)
$PASS = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($BSTR)

# Unique temp destination on the server
$ts = Get-Date -Format "yyyyMMddHHmmss"
$tmpRemote = "/tmp/$JarName.$ts"
$remoteJar = "$RemoteDir/$JarName"

# Upload
& pscp -batch -pw $PASS "$jar" "$User@${Server}:$tmpRemote"

# Remote commands
$cmds = @(
"set -e",
"mkdir -p '$RemoteDir'",
"systemctl stop $ServiceName || true",
"[ -f '$remoteJar' ] && cp -f '$remoteJar' '${remoteJar}.bak.$ts' || true",
"mv -f '$tmpRemote' '$remoteJar'",
"chown $Owner '$remoteJar' || true",
"chmod 640 '$remoteJar' || true",
"systemctl start $ServiceName",
"sleep 2",
"systemctl status $ServiceName --no-pager -l || true",
"journalctl -u $ServiceName -n 50 --no-pager || true"
) -join " ; "
$cmds = $cmds -replace "`r",""  # strip CRs

# Execute remotely
& plink -batch -pw $PASS "$User@$Server" "$cmds"

Write-Host "Deployment complete." -ForegroundColor Green
