@echo off
echo "Sorting..."
java -jar ircLogs.jar > nul
echo "Unix to Windows"
sfk176 addcr logs -yes
echo "Done"
pause > nul