# Compile all Java files into /out
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName

# Copy stylesheet to compiled output
Copy-Item src/src/styles.css -Destination out/src/ -Force

# Run JavaFX app
java -cp out src.Main
