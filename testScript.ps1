Write-Host "Begin testing..."

# Root dir of the java and python app
$javaRootDir = "C:\Users\leonp\IdeaProjects\py4jTest"
$pythonRootDir = "C:\Users\leonp\PycharmProjects\py4jTets"
# The size of the observationVector you want to test with. More means more data being send.
$observationVectorSize = "200"

# Activate venv
."$($pythonRootDir)\.venv\Scripts\activate.ps1"

for ($i = 0; $i -lt 20; $i++) {
    Write-Host "$($i). test round"

    $pythonDir = ""
    $javaDir = ""
    # flip flop beetween http and py4J test
    if ($i % 2 -eq 0) {
        # Http test
        Write-Host "Http test round..."
        $pythonDir = "\httpTest\httpEnvironment.py"
        $javaDir = "\out\artifacts\httpTestJava_jar\httpTestJava.jar"

    } else {
        # py4J test
        Write-Output "Py4J test round.."
        $pythonDir = "\py4JTest\main.py"
        $javaDir = "\out\artifacts\py4JTestJava_jar\py4JTestJava.jar"
    }

    # Start Python process and capture its process object
    $pythonProcess = Start-Process -FilePath python `
        -ArgumentList "`"$($pythonRootDir)$($pythonDir)`"", "$($observationVectorSize)" `
        -PassThru

    Start-Sleep -Seconds 5

    # Start Java process and capture its process object
    $javaProcess = Start-Process -FilePath java `
        -ArgumentList "-jar", "`"$($javaRootDir)$($javaDir)`"", "$($observationVectorSize)" `
        -PassThru
   
    Start-Sleep -Seconds 1.5

    # Wait until both processes are no longer running
    Wait-Process -Id $pythonProcess.Id, $javaProcess.Id

    Start-Sleep -Seconds 1.5
}


Write-Host "Test complete."
# Deactivate venv
deactivate