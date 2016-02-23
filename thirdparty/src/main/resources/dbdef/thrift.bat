set thrift=thrift-0.9.2.exe

set def=(user)
for %%A in %def% do %thrift% -out ../../java -r -v --gen java:private-members= %%A.thrift
REM for %%A in %def% do %thrift% -out ../../dbcenter/def -r -v --gen cpp:pure_enums=,include_prefix= %%A.thrift

pause
