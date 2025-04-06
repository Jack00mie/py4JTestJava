Write-Host "Begin testing..."

# Activate venv
."C:\Users\Leon Püschel\PycharmProjects\py4jTest\.venv\Scripts\activate.ps1"

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
        -ArgumentList "`"C:\Users\Leon Püschel\PycharmProjects\py4jTest$($pythonDir)`"", "200" `
        -PassThru

    Start-Sleep -Seconds 5

    # Start Java process and capture its process object
    $javaProcess = Start-Process -FilePath java `
        -ArgumentList "-jar", "`"C:\Users\Leon Püschel\IdeaProjects\py4JTestJava$($javaDir)`"", "200" `
        -PassThru
   
    Start-Sleep -Seconds 1.5

    # Wait until both processes are no longer running
    Wait-Process -Id $pythonProcess.Id, $javaProcess.Id

    Start-Sleep -Seconds 1.5
}


Write-Host "Test complete."
# Deactivate venv
deactivate