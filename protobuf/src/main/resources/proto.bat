D:
REM cd D:\project\face2face\protobuf\src\main\resources
cd D:\MyProject\IM\face2face\protobuf\src\main\resources

set OUT=../java
set def_java=(login)

for %%A in %def_java% do (
    echo generate java code: %%A.proto
    protoc.exe --java_out=%OUT% ./cli_def/%%A.proto
)

pause